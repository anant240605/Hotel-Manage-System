package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    private Long id;
    private int roomNumber;
    private BigDecimal pricePerNight;
    private int capacity;
    private RoomType roomType;
    private String available;

}