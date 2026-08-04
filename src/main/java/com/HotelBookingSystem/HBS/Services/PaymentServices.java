package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import com.HotelBookingSystem.HBS.Entity.Payment;
import com.HotelBookingSystem.HBS.Entity.PaymentStatus;
import com.HotelBookingSystem.HBS.Exception.PaymentException;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import com.HotelBookingSystem.HBS.Repository.PaymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServices {
    private final EmailService emailService;
    private final PaymentRepo paymentRepository;
    private final BookingRepo bookingRepository;
    private final PaymentGatewayFactory paymentGatewayFactory;

    @Transactional
    public void makePayment(PaymentRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new PaymentException("Booking Not Found"));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new PaymentException("Booking Already Paid");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new PaymentException("Booking Cancelled");
        }

        if (paymentRepository.findByBookingId(request.getBookingId()).isPresent()) {
            throw new PaymentException("Payment Already Exists");
        }

        PaymentGateway paymentGateway =
                paymentGatewayFactory.getPaymentGateway(
                        request.getPaymentMethod()
                );

        Payment payment =
                paymentGateway.processPayment(
                        booking,
                        request
                );

        booking.setStatus(BookingStatus.CONFIRMED);

        paymentRepository.save(payment);

        bookingRepository.save(booking);
        emailService.sendBookingConfirmation(booking);

    }


    public Payment getPaymentByBooking(Long bookingId) {

        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new PaymentException("Payment Not Found"));

    }

    @Transactional
    public  void refundPayment(Long bookingId) {

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new PaymentException("Payment Not Found"));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new PaymentException("Payment Already Refunded");
        }

        Booking booking = payment.getBooking();

        payment.setStatus(PaymentStatus.REFUNDED);

        booking.setStatus(BookingStatus.CANCELLED);

        paymentRepository.save(payment);

        bookingRepository.save(booking);


    }

}
