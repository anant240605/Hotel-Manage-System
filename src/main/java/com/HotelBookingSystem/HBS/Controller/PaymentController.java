package com.HotelBookingSystem.HBS.Controller;
import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import com.HotelBookingSystem.HBS.Services.PaymentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    private  final PaymentServices paymentServices;
    @PostMapping
    public ResponseEntity<?> makePayment(
            @RequestBody PaymentRequest request) {
        try {
            paymentServices.makePayment(request);
            return new ResponseEntity<>("Payment Successfull Conformation details will be send to your email ", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(
            @PathVariable Long bookingId) {
        try {
            return ResponseEntity.ok(paymentServices.getPaymentByBooking(bookingId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/refund/{bookingId}")
    public ResponseEntity<?> refundPayment(
            @PathVariable Long bookingId) {
        try {
            paymentServices.refundPayment(bookingId);
            return new ResponseEntity<>("Payment will be refunded", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }
}
