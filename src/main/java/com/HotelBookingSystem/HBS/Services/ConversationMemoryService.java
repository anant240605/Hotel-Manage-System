package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.DTO.ConversationContext;

public interface ConversationMemoryService {

    void save(String sessionId, ConversationContext context);

    ConversationContext get(String sessionId);

    void delete(String sessionId);

}