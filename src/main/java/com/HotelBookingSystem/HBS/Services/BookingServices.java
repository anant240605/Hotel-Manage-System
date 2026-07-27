package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.DTO.BookingRequest;
import com.HotelBookingSystem.HBS.Entity.*;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.RoomRepo;
import com.HotelBookingSystem.HBS.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


@Service
public class BookingServices {
    @Autowired
     private BookingRepo bookingRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private HotelRepo hotelRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private PaymentServices paymentServices;

    @Transactional
    public Booking createBooking(BookingRequest request){

        User user = userRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Hotel hotel = hotelRepo.findByName(request.getHotelName())
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));

        List<Room> rooms =
                roomRepo.findByHotelIdAndRoomType(
                        hotel.getId(),
                        request.getRoomType());

        if(rooms.isEmpty()){
            throw new RuntimeException("No Room Found");
        }

        Room selectedRoom = null;

        for(Room room : rooms){

            List<Booking> bookings =
                    bookingRepo.findByRoomIdAndStatus(
                            room.getId(),
                            BookingStatus.PENDING);

            boolean overlap = false;

            for(Booking booking : bookings){

                if(request.getCheckInDate().isBefore(booking.getCheckOutDate())
                        &&
                        request.getCheckOutDate().isAfter(booking.getCheckInDate())){

                    overlap = true;
                    break;

                }

            }

            if(!overlap){
                selectedRoom = room;
                break;
            }

        }

        if(selectedRoom == null){
            throw new RuntimeException("No Room Available For Selected Dates");
        }

        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());

        if(days <= 0){
            throw new RuntimeException("Invalid Dates");
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



        return bookingRepo.save(booking);

    }
   public Optional<Booking>  getBookingByBookingId(Long id){

        if(!bookingRepo.existsById(id)){
             throw  new RuntimeException("Booking not found");
        }
       Optional<Booking> booking=bookingRepo.findById(id);
      return booking;
   }

    public List<Booking> getBookingsByUser(Long userId) {

        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("User Not Found");
        }

        return bookingRepo.findByUserId(userId);
    }





    @Transactional
    public Booking updateBooking(Long bookingId,
                                 BookingRequest request){

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking Not Found"));

        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new RuntimeException("Cancelled Booking Cannot Be Updated");
        }

        if(!request.getCheckOutDate().isAfter(request.getCheckInDate())){
            throw new RuntimeException("Check-out must be after Check-in");
        }

        Room room = booking.getRoom();

        List<Booking> bookings =
                bookingRepo.findByRoomIdAndStatus(
                        room.getId(),
                        BookingStatus.CONFIRMED);

        for(Booking b : bookings){

            if(b.getId().equals(bookingId)){
                continue;
            }

            if(request.getCheckInDate().isBefore(b.getCheckOutDate())
                    &&
                    request.getCheckOutDate().isAfter(b.getCheckInDate())){

                throw new RuntimeException(
                        "Room already booked for selected dates");
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

        return bookingRepo.save(booking);

    }

    @Transactional
    public Booking cancelBooking(Long bookingId){

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking Not Found"));

        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new RuntimeException("Booking Already Cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepo.save(booking);

    }

}
