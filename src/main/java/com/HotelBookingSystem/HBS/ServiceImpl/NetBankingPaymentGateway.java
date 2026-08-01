package com.HotelBookingSystem.HBS.ServiceImpl;

import com.HotelBookingSystem.HBS.DTO.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class NetBankingPaymentGateway
        extends AbstractPaymentGateway {

    @Override
    protected void validatePayment(
            PaymentRequest request) {

        System.out.println("Processing Net Banking Payment...");

        //     can implement validation and razorpay in future


    }

}