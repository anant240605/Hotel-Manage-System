package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateBookingRequest {

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
