package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.Payment;

public interface PaymentGateway {
    Payment processPayment(Booking booking, PaymentRequest paymentRequest);
}
