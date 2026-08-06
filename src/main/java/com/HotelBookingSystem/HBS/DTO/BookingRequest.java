package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingRequest {

    private Long userId;

    private Long hotelId;

    private Long roomCategoryId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;


}
