package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.DTO.BookingRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Services.BookingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServices bookingServices;

    @PostMapping("create-booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingServices.createBooking(request);
            return new ResponseEntity<>(
                    Map.of("message", "Booking Created", "bookingId", booking.getId()),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getBookingsByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(bookingServices.getBookingsByUser(userId));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingStatus(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingServices.getBookingByBookingId(id);
            return new ResponseEntity<>(booking.get().getStatus(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingServices.cancelBooking(bookingId);
            return new ResponseEntity<>("Booking Cancelled", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable Long bookingId, @RequestBody BookingRequest request) {
        try {
            bookingServices.updateBooking(bookingId, request);
            return new ResponseEntity<>("Booking Updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
