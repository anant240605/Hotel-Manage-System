package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.DTO.BookingRequest;
import com.HotelBookingSystem.HBS.Entity.*;
import com.HotelBookingSystem.HBS.Exception.BookingException;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.RoomRepo;
import com.HotelBookingSystem.HBS.Repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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

    @Transactional
    public Booking createBooking(BookingRequest request) {
        String lockKey = null;
        boolean locked = false;
        try {

            User user = userRepo.findById(request.getId())
                    .orElseThrow(() -> new BookingException(MessageConstants.USER_NOT_FOUND));

            Hotel hotel = hotelRepo.findByName(request.getHotelName())
                    .orElseThrow(() -> new BookingException(MessageConstants.HOTEL_NOT_FOUND));

            lockKey = "booking:hotel:" + hotel.getId() + ":" + request.getRoomType();
            locked = redisLockService.acquireLock(lockKey);

            if (!locked) {
                throw new BookingException(
                        MessageConstants.BOOKING_IN_PROGRESS
                );
            }

            List<Room> rooms =
                    roomRepo.findByHotelIdAndRoomType(
                            hotel.getId(),
                            request.getRoomType());

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

            BigDecimal totalPrice =
                    selectedRoom.getPricePerNight()
                            .multiply(BigDecimal.valueOf(days));

            Booking booking = new Booking();

            booking.setUser(user);
            booking.setRoom(selectedRoom);
            booking.setHotelName(hotel.getName());
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

        Room room = booking.getRoom();

        List<Booking> bookings =
                bookingRepo.findByRoomIdAndStatus(
                        room.getId(),
                        BookingStatus.CONFIRMED);

        for (Booking b : bookings) {

            if (b.getId().equals(bookingId)) {
                continue;
            }

            if (request.getCheckInDate().isBefore(b.getCheckOutDate())
                    &&
                    request.getCheckOutDate().isAfter(b.getCheckInDate())) {

                throw new BookingException(
                        MessageConstants.ROOM_ALREADY_BOOKED);
            }

        }

        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());

        BigDecimal totalPrice =
                room.getPricePerNight()
                        .multiply(BigDecimal.valueOf(days));

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(totalPrice);

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

}
