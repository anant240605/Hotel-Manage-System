package com.HotelBookingSystem.HBS.DTO;

import lombok.Data;

@Data
public class AIChatRequest {
    private Long userId;
    private String message;
    private String sessionId;

}