package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.DTO.*;
import com.HotelBookingSystem.HBS.Services.RoomCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class RoomCategory {
    private final RoomCategoryService roomCategoryService;

    @PostMapping
    public RoomCategoryResponse createCategory(@RequestBody RoomCategoryRequest request){

        return roomCategoryService.createCategory(request);

    }

    @GetMapping("{hotelId}")
    public List<RoomCategoryResponse> getCategories(@PathVariable Long hotelId){
        return roomCategoryService.getCategories(hotelId);

    }
    @PutMapping("/{categoryId}")
    public RoomCategoryResponse updateCategory(

            @PathVariable Long categoryId,

            @RequestBody UpdateRoomCategoryRequest request){

        return roomCategoryService
                .updateCategory(categoryId,request);

    }

    @PatchMapping("/{categoryId}/price")
    public RoomCategoryResponse updatePrice(@PathVariable Long categoryId, @RequestBody UpdatePriceRequest request){

        return roomCategoryService.updatePrice(categoryId, request);

    }
    @GetMapping("/{hotelId}/inventory")
    public List<RoomCategoryInventoryResponse> inventory(@PathVariable Long hotelId){
        return roomCategoryService.getInventory(hotelId);

    }
}
