package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.DTO.CreateRoomRequest;
import com.HotelBookingSystem.HBS.DTO.RoomResponse;
import com.HotelBookingSystem.HBS.DTO.UpdateRoomRequest;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Entity.RoomCategory;
import com.HotelBookingSystem.HBS.Exception.RoomException;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.RoomCategoryRepository;
import com.HotelBookingSystem.HBS.Repository.RoomRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomServices {
    private final RoomRepo roomRepo;
    private final HotelRepo hotelRepo;
    private final RoomCategoryRepository roomCategoryRepository;

    public void createRoom(Long hotelId, CreateRoomRequest request) {

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new RoomException(MessageConstants.HOTEL_NOT_FOUND));
        RoomCategory category= roomCategoryRepository.findById(request.getCategoryId()).orElseThrow(()->new RoomException("Category not found"));
        if(!category.getHotel().getId().equals(hotelId)){
            throw new RoomException("Category does not belong to this hotel");
        }
        Room room =
                Room.builder()
                        .roomNumber(request.getRoomNumber())
                        .hotel(hotel)
                        .roomCategory(category)
                        .build();

         roomRepo.save(room);
    }

    public List<RoomResponse> getRooms(Long hotelId) {

        if (!hotelRepo.existsById(hotelId)) {
            throw new RoomException(MessageConstants.HOTEL_NOT_FOUND);
        }
        List<Room> rooms = roomRepo.findByHotelId(hotelId);

        return rooms.stream()
                .map(room ->
                        RoomResponse.builder()

                                .id(room.getId())
                                .roomNumber(room.getRoomNumber())
                                .roomType(room.getRoomCategory().getRoomType())
                                .pricePerNight(room.getRoomCategory().getPricePerNight())
                                .capacity(room.getRoomCategory().getCapacity())
                                .build()
                )

                .toList();
    }

    public void updateRoom(Long hotelId, Long roomId, UpdateRoomRequest room) {

            Room existingRoom = roomRepo.findByIdAndHotelId(roomId, hotelId);
            if (existingRoom == null) {
                throw new RoomException(MessageConstants.ROOM_NOT_FOUND);
            }


            if (room.getRoomNumber() != null) {
                existingRoom.setRoomNumber(room.getRoomNumber());
            }


            if(room.getCategoryId()!=null){
                RoomCategory category = roomCategoryRepository.findById(room.getCategoryId()).orElseThrow(()->new RoomException("Category Not Found"));

                if(!category.getHotel().getId().equals(hotelId)){
                    throw new RoomException("Category belongs to another hotel");
                }
                existingRoom.setRoomCategory(category);
            }

            roomRepo.save(existingRoom);



    }

    @Transactional
    public void deleteRoom(Long hotelId, Long roomId) {
        try {
            Room room = roomRepo.findByIdAndHotelId(roomId, hotelId);
            if (Objects.nonNull(room)) {
                roomRepo.delete(room);
            }

        } catch (Exception e) {
            throw new RoomException(MessageConstants.SOMETHING_WENT_WRONG);

        }


    }
}
