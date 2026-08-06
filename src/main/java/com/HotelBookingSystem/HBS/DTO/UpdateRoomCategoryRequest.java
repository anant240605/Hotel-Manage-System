package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoomCategoryRequest {
    private String name;

    private BigDecimal pricePerNight;

    private Integer capacity;
}
