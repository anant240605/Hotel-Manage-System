package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.DTO.*;
import com.HotelBookingSystem.HBS.Entity.RoomCategory;

import java.math.BigDecimal;
import java.util.List;

public interface RoomCategoryService {
    RoomCategoryResponse createCategory(RoomCategoryRequest request);

    List<RoomCategoryResponse> getCategories(Long hotelId);

    RoomCategoryResponse updatePrice(Long categoryId, UpdatePriceRequest request);
    RoomCategoryResponse updateCategory(Long categoryId, UpdateRoomCategoryRequest request);
    List<RoomCategoryInventoryResponse> getInventory(Long hotelId);

}
