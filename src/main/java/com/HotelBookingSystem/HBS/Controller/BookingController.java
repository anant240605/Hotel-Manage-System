package com.HotelBookingSystem.HBS.Controller;
import com.HotelBookingSystem.HBS.DTO.BookingRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Services.BookingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServices bookingServices;

    @PostMapping("create-booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {

            Booking booking = bookingServices.createBooking(request);
            return new ResponseEntity<>(
                    Map.of("message", "Booking Created", "bookingId", booking.getId()),
                    HttpStatus.CREATED);

    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getBookingsByUser(@PathVariable Long userId) {

            return ResponseEntity.ok(bookingServices.getBookingsByUser(userId));


    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingStatus(@PathVariable Long id) {

            Booking booking = bookingServices.getBookingByBookingId(id).orElseThrow(()->new RuntimeException("Booking not found"));

            return ResponseEntity.ok(booking.getStatus());

    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {

            bookingServices.cancelBooking(bookingId);
            return new ResponseEntity<>("Booking Cancelled", HttpStatus.OK);


    }


    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable Long bookingId, @RequestBody BookingRequest request) {

            bookingServices.updateBooking(bookingId, request);
            return new ResponseEntity<>("Booking Updated", HttpStatus.OK);

    }

}
