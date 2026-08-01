package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
    private  Long bookingId;
    private PaymentMethod paymentMethod;

}
