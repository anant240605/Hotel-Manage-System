package com.HotelBookingSystem.HBS.Services;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.RoomRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServices {

    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private HotelRepo hotelRepo;


    public Room createRoom(Long hotelId, Room room){

        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));

        room.setHotel(hotel);

        return roomRepo.save(room);
    }
    public List<Room> getRooms(Long hotelId){

        if(!hotelRepo.existsById(hotelId)){
            throw new RuntimeException("Hotel Not Found");
        }

        return roomRepo.findByHotelId(hotelId);
    }

    public ResponseEntity<?> updateRoom(Long hotelId, Long roomId, Room room){
       try{
           Room existingRoom = roomRepo.findByIdAndHotelId(roomId,hotelId);
           if(existingRoom==null){
               return new ResponseEntity<>("Room not Found", HttpStatus.NOT_FOUND);
           }


           if(room.getRoomNumber()!=0){
               existingRoom.setRoomNumber(room.getRoomNumber());
           }

           if(room.getCapacity()!=0){
               existingRoom.setCapacity(room.getCapacity());
           }


           existingRoom.setPricePerNight(room.getPricePerNight());
           existingRoom.setAvailable(room.getAvailable());
           existingRoom.setRoomType(room.getRoomType());

           roomRepo.save(existingRoom);
           return  new ResponseEntity<>( HttpStatus.OK);

       }catch (Exception e){
           throw new RuntimeException("Something went wrong");
       }

    }

    @Transactional
    public void deleteRoom(Long hotelId, Long roomId){
    try{
        Room room = roomRepo.findByIdAndHotelId(roomId,hotelId);
        roomRepo.delete(room);

    }catch (Exception e){
        throw  new RuntimeException("Something went wrong");

    }



    }
}
