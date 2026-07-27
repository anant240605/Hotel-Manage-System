package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.RoomType;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BookingRequest {

        private Long id;

        private String hotelName;

        private RoomType roomType;

        private LocalDate checkInDate;

        private LocalDate checkOutDate;


}
