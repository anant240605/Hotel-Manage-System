package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Exception.ReviewException;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Services.RoomServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomServices roomServices;
    private final HotelRepo hotelRepo;

    @PostMapping("/hotels/{hotelId}")
    public ResponseEntity<?> createRoom(@PathVariable Long hotelId,
                                        @RequestBody Room room) {

        roomServices.createRoom(hotelId, room);
        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(()->new ReviewException(MessageConstants.HOTEL_NOT_FOUND));

        String name = hotel.getName();
        return new ResponseEntity<>("Room Created for " + name, HttpStatus.OK);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getRooms(@PathVariable Long hotelId) {

        return ResponseEntity.ok(roomServices.getRooms(hotelId));

    }

    @PutMapping("/{hotelId}/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long hotelId,
                                        @PathVariable Long roomId,
                                        @RequestBody Room room) {
        roomServices.updateRoom(hotelId, roomId, room);
        return new ResponseEntity<>("Room Updated ", HttpStatus.OK);


    }

    @DeleteMapping("/{hotelId}/{roomId}")
    public void deleteRoom(@PathVariable Long hotelId,
                           @PathVariable Long roomId) {
        try {
            roomServices.deleteRoom(hotelId, roomId);

        } catch (Exception e) {
            throw new ReviewException(e.getMessage());
        }


    }


}
