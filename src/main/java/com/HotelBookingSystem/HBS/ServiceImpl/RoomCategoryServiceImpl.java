package com.HotelBookingSystem.HBS.ServiceImpl;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.DTO.*;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.RoomCategory;
import com.HotelBookingSystem.HBS.Exception.HotelException;
import com.HotelBookingSystem.HBS.Exception.RoomException;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.RoomCategoryRepository;
import com.HotelBookingSystem.HBS.Repository.RoomRepo;
import com.HotelBookingSystem.HBS.Services.RoomCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomCategoryServiceImpl implements RoomCategoryService {
    private final RoomCategoryRepository roomCategoryRepository;
  private final RoomRepo roomRepo;
    private final HotelRepo hotelRepository;


    @Override
    public RoomCategoryResponse createCategory(RoomCategoryRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId()).orElseThrow(()->new HotelException("Hotel Not Found"));
        RoomCategory category =
                RoomCategory.builder()
                        .name(request.getName())
                        .roomType(request.getRoomType())
                        .pricePerNight(request.getPricePerNight())
                        .capacity(request.getCapacity())
                        .hotel(hotel)
                        .build();

        roomCategoryRepository.save(category);

        return RoomCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .roomType(category.getRoomType())
                .pricePerNight(category.getPricePerNight())
                .capacity(category.getCapacity())
                .build();
    }

    @Override
    public List<RoomCategoryResponse> getCategories(Long hotelId) {
        if(!hotelRepository.existsById(hotelId)){
            throw new HotelException(
                    MessageConstants.HOTEL_NOT_FOUND);
        }
        List<RoomCategory> categories = roomCategoryRepository.findByHotelId(hotelId);
        return categories.stream()

                .map(category ->
                        RoomCategoryResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .roomType(category.getRoomType())
                                .pricePerNight(category.getPricePerNight())
                                .capacity(category.getCapacity())
                                .build())

                .toList();

    }

    @Override
    public RoomCategoryResponse updatePrice(Long categoryId, UpdatePriceRequest request) {
        RoomCategory category = roomCategoryRepository.findById(categoryId).orElseThrow(() -> new RoomException("Category Not Found"));

        category.setPricePerNight(request.getPricePerNight());
         roomCategoryRepository.save(category);

        return RoomCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .roomType(category.getRoomType())
                .pricePerNight(category.getPricePerNight())
                .capacity(category.getCapacity())
                .build();
    }

    @Override
    public RoomCategoryResponse updateCategory(Long categoryId, UpdateRoomCategoryRequest request) {
        RoomCategory category = roomCategoryRepository.findById(categoryId).orElseThrow(() -> new RoomException("Category Not Found"));
        if(request.getName()!=null){
            category.setName(request.getName());
        }

        if(request.getPricePerNight()!=null){
            category.setPricePerNight(
                    request.getPricePerNight());
        }

        if(request.getCapacity()!=null){
            category.setCapacity(
                    request.getCapacity());
        }

        category = roomCategoryRepository.save(category);

        return RoomCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .roomType(category.getRoomType())
                .pricePerNight(category.getPricePerNight())
                .capacity(category.getCapacity())
                .build();

    }

    @Override
    public List<RoomCategoryInventoryResponse> getInventory(Long hotelId) {
        List<RoomCategory> categories = roomCategoryRepository.findByHotelId(hotelId);

        return categories.stream()

                .map(category -> {
                    long total = roomRepo.countByRoomCategoryId(category.getId());

                    return RoomCategoryInventoryResponse.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .roomType(category.getRoomType())
                            .pricePerNight(category.getPricePerNight())
                            .capacity(category.getCapacity())
                            .totalRooms(total)
                            .build();

                })

                .toList();
    }


}

