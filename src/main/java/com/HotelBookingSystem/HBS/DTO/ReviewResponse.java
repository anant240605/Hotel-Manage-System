package com.HotelBookingSystem.HBS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewResponse {

    private Long id;

    private String userName;

    private Integer rating;

    private String review;

    private LocalDateTime createdAt;

}