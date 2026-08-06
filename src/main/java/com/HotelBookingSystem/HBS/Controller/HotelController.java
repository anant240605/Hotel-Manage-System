package com.HotelBookingSystem.HBS.Controller;
import com.HotelBookingSystem.HBS.DTO.HotelResponse;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Services.HotelServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelServices hotelServices;

    @PostMapping("create-hotel")
    public ResponseEntity<?> saveHotel(@RequestBody Hotel hotel) {

            hotelServices.saveHotel(hotel);
            return new ResponseEntity<>("Hotel Created", HttpStatus.CREATED);


    }

    @GetMapping("{city}")
    public ResponseEntity<?> getHotelByCity(@PathVariable String city) {

            List<HotelResponse> hotel = hotelServices.getHotelByCity(city);
            return new ResponseEntity<>(hotel, HttpStatus.OK);


    }

    @GetMapping
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelServices.getAllHotels());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id,
                                         @RequestBody Hotel hotel) {

            hotelServices.updateHotel(id, hotel);
            return ResponseEntity.ok("Hotel Updated Successfully");


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {

        hotelServices.deleteHotel(id);

        return ResponseEntity.ok("Hotel Deleted Successfully");

    }


}
