package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Services.RoomServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomServices roomServices;
    @Autowired
    private HotelRepo hotelRepo;

    @PostMapping("/hotels/{hotelId}")
    public ResponseEntity<?> createRoom(@PathVariable Long hotelId,
                                        @RequestBody Room room){

        roomServices.createRoom(hotelId,room);
        Optional<Hotel> hotel= hotelRepo.findById(hotelId);
        String name= hotel.get().getName();
        return new ResponseEntity<>("Room Created for "+ name,HttpStatus.OK);

    }
    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getRooms(@PathVariable Long hotelId){

        return ResponseEntity.ok(roomServices.getRooms(hotelId));

    }

    @PutMapping("/{hotelId}/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long hotelId,
                                        @PathVariable Long roomId,
                                        @RequestBody Room room){
      roomServices.updateRoom(hotelId,roomId,room);
       return new ResponseEntity<>("Room Updated ", HttpStatus.OK);


    }
    @DeleteMapping("/{hotelId}/{roomId}")
    public void deleteRoom(@PathVariable Long hotelId,
                                        @PathVariable Long roomId){
        try{
            roomServices.deleteRoom(hotelId,roomId);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }



    }


}
