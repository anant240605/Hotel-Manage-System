package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.DTO.BookingRequest;
import com.HotelBookingSystem.HBS.Entity.*;
import com.HotelBookingSystem.HBS.Exception.BookingException;
import com.HotelBookingSystem.HBS.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServices {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final HotelRepo hotelRepo;
    private final RoomRepo roomRepo;
    private final RedisLockService redisLockService;
    private  final RoomCategoryRepository roomCategoryRepository;

    @Transactional
    public Booking createBooking(BookingRequest request) {
        String lockKey = null;
        boolean locked = false;
        try {

            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new BookingException(MessageConstants.USER_NOT_FOUND));

            Hotel hotel = hotelRepo.findById(request.getHotelId())
                    .orElseThrow(() -> new BookingException(MessageConstants.HOTEL_NOT_FOUND));
            RoomCategory category = roomCategoryRepository.findById(request.getRoomCategoryId()).orElseThrow(()->new BookingException("Room Category Not Found"));
            if(!category.getHotel().getId().equals(hotel.getId())){
                throw new BookingException("Room Category does not belong to this hotel");

            }
            lockKey = "booking:hotel:" + hotel.getId() + ":category:" + request.getRoomCategoryId();
            locked = redisLockService.acquireLock(lockKey);



            if (!locked) {
                throw new BookingException(
                        MessageConstants.BOOKING_IN_PROGRESS
                );
            }

            List<Room> rooms = roomRepo.findByRoomCategoryId(category.getId());

            if (rooms.isEmpty()) {
                throw new BookingException(MessageConstants.ROOM_NOT_FOUND);
            }

            Room selectedRoom = null;

            for (Room room : rooms) {

                List<Booking> bookings =
                        bookingRepo.findByRoomIdAndStatusIn(
                                room.getId(),
                                List.of(
                                        BookingStatus.PENDING,
                                        BookingStatus.CONFIRMED
                                ));

                boolean overlap = false;

                for (Booking booking : bookings) {

                    if (request.getCheckInDate().isBefore(booking.getCheckOutDate())
                            &&
                            request.getCheckOutDate().isAfter(booking.getCheckInDate())) {

                        overlap = true;
                        break;

                    }

                }

                if (!overlap) {
                    selectedRoom = room;

                    break;
                }

            }

            if (selectedRoom == null) {
                throw new BookingException(MessageConstants.ROOM_NOT_AVAILABLE);
            }

            long days = ChronoUnit.DAYS.between(
                    request.getCheckInDate(),
                    request.getCheckOutDate());

            if (days <= 0) {
                throw new BookingException(MessageConstants.INVALID_DATES);
            }

            BigDecimal totalPrice = category.getPricePerNight().multiply(BigDecimal.valueOf(days));

            Booking booking = new Booking();

            booking.setUser(user);
            booking.setRoom(selectedRoom);
            booking.setCheckInDate(request.getCheckInDate());
            booking.setCheckOutDate(request.getCheckOutDate());
            booking.setStatus(BookingStatus.PENDING);
            booking.setTotalPrice(totalPrice);
            booking.setCreatedAt(LocalDateTime.now());

            try {
                Thread.sleep(10000); // 10 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return bookingRepo.save(booking);


        } finally {
            if (locked) {
                redisLockService.releaseLock(lockKey);

            }
        }

    }


    public Optional<Booking> getBookingByBookingId(Long id) {

        if (!bookingRepo.existsById(id)) {
            throw new BookingException(MessageConstants.BOOKING_NOT_FOUND);
        }
        return bookingRepo.findById(id);
    }

    public List<Booking> getBookingsByUser(Long userId) {

        if (!userRepo.existsById(userId)) {
            throw new BookingException(MessageConstants.USER_NOT_FOUND);
        }

        return bookingRepo.findByUserId(userId);
    }


    @Transactional
    public void updateBooking(Long bookingId,
                                 BookingRequest request) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow();

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException(MessageConstants.CANCELLED_BOOKING_UPDATE);
        }

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new BookingException(MessageConstants.INVALID_CHECKOUT);
        }


        Hotel hotel = booking.getRoom().getHotel();

        RoomCategory category =
                roomCategoryRepository.findById(
                                request.getRoomCategoryId())
                        .orElseThrow(() ->
                                new BookingException(
                                        "Room Category Not Found"));
        if(!category.getHotel().getId().equals(hotel.getId()))
        {
            throw new BookingException("Category does not belong to this hotel");
        }
        Room selectedRoom = findAvailableRoom(
                category.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                bookingId
        );

        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());

        BigDecimal totalPrice =
                selectedRoom.getRoomCategory().getPricePerNight().multiply(BigDecimal.valueOf(days));

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setRoom(selectedRoom);
        booking.setTotalPrice(totalPrice);
        bookingRepo.save(booking);

    }

    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new BookingException(MessageConstants.BOOKING_NOT_FOUND));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException(MessageConstants.BOOKING_ALREADY_CANCELLED);
        }

        booking.setStatus(BookingStatus.CANCELLED);
    }

    private Room findAvailableRoom(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut, Long currentBookingId) {

        List<Room> rooms = roomRepo.findByRoomCategoryId(roomCategoryId);

        for (Room room : rooms) {
            List<Booking> bookings = bookingRepo.findByRoomIdAndStatusIn(room.getId(),

                            List.of(
                                    BookingStatus.PENDING,
                                    BookingStatus.CONFIRMED
                            )

                    );

            boolean overlap = false;

            for (Booking booking : bookings) {

                if (currentBookingId != null &&
                        booking.getId().equals(currentBookingId)) {
                    continue;
                }

                if (checkIn.isBefore(booking.getCheckOutDate())
                        &&
                        checkOut.isAfter(booking.getCheckInDate())) {

                    overlap = true;
                    break;
                }

            }

            if (!overlap) {
                return room;
            }

        }

        throw new BookingException(
                MessageConstants.ROOM_NOT_AVAILABLE);
    }

}
