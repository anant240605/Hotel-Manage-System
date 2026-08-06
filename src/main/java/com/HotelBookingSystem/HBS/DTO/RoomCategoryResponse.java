package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategoryResponse {
    private Long id;

    private String name;

    private RoomType roomType;

    private BigDecimal pricePerNight;

    private Integer capacity;

}
