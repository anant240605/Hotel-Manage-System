package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import com.HotelBookingSystem.HBS.Entity.Payment;
import com.HotelBookingSystem.HBS.Entity.PaymentStatus;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import com.HotelBookingSystem.HBS.Repository.PaymentRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServices {

    @Autowired
    private PaymentRepo paymentRepository;

    @Autowired
    private BookingRepo bookingRepository;

    @Transactional
    public Payment makePayment(PaymentRequest request){

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException("Booking Not Found"));

        if(booking.getStatus()== BookingStatus.CONFIRMED){
            throw new RuntimeException("Booking Already Paid");
        }

        if(booking.getStatus()==BookingStatus.CANCELLED){
            throw new RuntimeException("Booking Cancelled");
        }

        if(paymentRepository.findByBookingId(request.getBookingId()).isPresent()){
            throw new RuntimeException("Payment Already Exists");
        }

        Payment payment = new Payment();

        payment.setAmount(booking.getTotalPrice());

        payment.setPaymentMethod(request.getPaymentMethod());

        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setBooking(booking);

        booking.setStatus(BookingStatus.CONFIRMED);

        paymentRepository.save(payment);

        bookingRepository.save(booking);

        return payment;

    }

    public Payment getPayment(Long paymentId){

        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Found"));

    }

    public Payment getPaymentByBooking(Long bookingId){

        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Found"));

    }

    @Transactional
    public Payment refundPayment(Long bookingId){

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Found"));

        if(payment.getStatus()==PaymentStatus.REFUNDED){
            throw new RuntimeException("Payment Already Refunded");
        }

        Booking booking = payment.getBooking();

        payment.setStatus(PaymentStatus.REFUNDED);

        booking.setStatus(BookingStatus.CANCELLED);

        paymentRepository.save(payment);

        bookingRepository.save(booking);

        return payment;

    }

}
