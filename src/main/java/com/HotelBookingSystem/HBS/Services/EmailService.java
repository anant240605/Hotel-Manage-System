package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.Entity.Booking;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );

    void sendBookingConfirmation(Booking booking);

}