package com.HotelBookingSystem.HBS.Exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ReviewException.class,
            PaymentException.class,
            BookingException.class,
            HotelException.class,
            RoomException.class,
            UserException.class
    })
    public ResponseEntity<String> handleBusinessExceptions(RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

}