package com.HotelBookingSystem.HBS.DTO;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdateBookingRequest {

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
