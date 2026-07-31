package com.HotelBookingSystem.HBS.Controller;
import com.HotelBookingSystem.HBS.DTO.HotelResponse;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Services.HotelServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("hotel")
public class HotelController {
    @Autowired
    private HotelServices hotelServices;

    @PostMapping("create-hotel")
    public ResponseEntity<?> saveHotel(@RequestBody Hotel hotel){
        try{
            hotelServices.saveHotel(hotel);
            return new ResponseEntity<>("Hotel Created", HttpStatus.CREATED);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("{city}")
    public ResponseEntity<?> getHotelByCity(@PathVariable String city){
        try{
          List<HotelResponse> hotel =  hotelServices.getHotelByCity(city);
            return  new ResponseEntity<>(hotel, HttpStatus.OK);
        }catch (Exception e){
            return   ResponseEntity.badRequest().body(e.getMessage());

        }

    }
    @GetMapping
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelServices.getAllHotels());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id,
                                         @RequestBody Hotel hotel) {
        try{
            hotelServices.updateHotel(id, hotel);
            return ResponseEntity.ok("Hotel Updated Successfully");
        }catch (Exception e){
            throw new RuntimeException("Can not Update the hotel");
        }




    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {

        hotelServices.deleteHotel(id);

        return ResponseEntity.ok("Hotel Deleted Successfully");

    }



}
