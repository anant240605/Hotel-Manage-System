package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomRequest {
    private Integer roomNumber;

    private Long categoryId;

    private String available;
}
