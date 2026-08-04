package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Exception.RoomException;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
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

    public void createRoom(Long hotelId, Room room) {

        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RoomException(MessageConstants.HOTEL_NOT_FOUND));

        room.setHotel(hotel);

         roomRepo.save(room);
    }

    public List<Room> getRooms(Long hotelId) {

        if (!hotelRepo.existsById(hotelId)) {
            throw new RoomException(MessageConstants.HOTEL_NOT_FOUND);
        }

        return roomRepo.findByHotelId(hotelId);
    }

    public void updateRoom(Long hotelId, Long roomId, Room room) {
        try {
            Room existingRoom = roomRepo.findByIdAndHotelId(roomId, hotelId);
            if (existingRoom == null) {
                throw new RoomException(MessageConstants.ROOM_NOT_FOUND);
            }


            if (room.getRoomNumber() != 0) {
                existingRoom.setRoomNumber(room.getRoomNumber());
            }

            if (room.getCapacity() != 0) {
                existingRoom.setCapacity(room.getCapacity());
            }


            existingRoom.setPricePerNight(room.getPricePerNight());
            existingRoom.setAvailable(room.getAvailable());
            existingRoom.setRoomType(room.getRoomType());

            roomRepo.save(existingRoom);

        } catch (Exception e) {
            throw new RoomException(MessageConstants.SOMETHING_WENT_WRONG);
        }

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
