package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Double rating;

    private List<RoomResponse> rooms;
}
