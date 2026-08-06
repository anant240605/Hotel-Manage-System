package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomRequest {
    private Integer roomNumber;

    private String  available;

    private Long categoryId;
}
