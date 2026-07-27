package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {
    private  Long bookingId;
    private PaymentMethod paymentMethod;

}
