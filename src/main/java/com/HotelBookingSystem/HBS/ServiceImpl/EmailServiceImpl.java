package com.HotelBookingSystem.HBS.ServiceImpl;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async("emailExecutor")
    @Override
    public void sendBookingConfirmation(Booking booking) {

        String subject = "Booking Confirmation";

        String body =
                "Dear " + booking.getUser().getName() + ",\n\n" +

                        "Your booking has been confirmed.\n\n" +

                        "Booking ID : " + booking.getId() + "\n" +
                        "Hotel : " + booking.getHotelName() + "\n" +
                        "Room Number : " + booking.getRoom().getRoomNumber() + "\n" +
                        "Room Type : " + booking.getRoom().getRoomType() + "\n" +
                        "Check In : " + booking.getCheckInDate() + "\n" +
                        "Check Out : " + booking.getCheckOutDate() + "\n" +
                        "Total Price : ₹" + booking.getTotalPrice() + "\n\n" +

                        "Thank you for choosing us.";

        sendEmail(
                booking.getUser().getEmail(),
                subject,
                body
        );

    }


    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();


        message.setFrom(MessageConstants.SENT_FROM);

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }


}