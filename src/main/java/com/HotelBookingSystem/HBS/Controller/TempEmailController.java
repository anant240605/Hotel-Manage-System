package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.Services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class TempEmailController {
    private final EmailService emailService;

    @GetMapping("/test")
    public String testMail() {

        emailService.sendEmail(
                "anant8139@gmail.com",
                "Spring Boot Test",
                "Congratulations! Email configuration is working."
        );

        return "Email Sent Successfully";
    }
}
