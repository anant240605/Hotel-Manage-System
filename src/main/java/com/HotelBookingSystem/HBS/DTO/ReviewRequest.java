package com.HotelBookingSystem.HBS.DTO;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long bookingId;

    private Integer rating;

    private String review;
}
