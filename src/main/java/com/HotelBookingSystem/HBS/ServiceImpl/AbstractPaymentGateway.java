package com.HotelBookingSystem.HBS.ServiceImpl;
import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.Payment;
import com.HotelBookingSystem.HBS.Entity.PaymentStatus;
import com.HotelBookingSystem.HBS.Services.PaymentGateway;

public abstract class AbstractPaymentGateway
        implements PaymentGateway {

    @Override
    public Payment processPayment(
            Booking booking,
            PaymentRequest request) {

        validatePayment(request);

        Payment payment = new Payment();

        payment.setAmount(booking.getTotalPrice());

        payment.setPaymentMethod(request.getPaymentMethod());

        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setBooking(booking);

        return payment;

    }

    protected abstract void validatePayment(
            PaymentRequest request);

}