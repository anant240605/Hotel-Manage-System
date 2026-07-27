package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServices {
    @Autowired
    private HotelRepo hotelRepo;

    public Hotel saveHotel(Hotel hotel){
        if(hotelRepo.existsByName(hotel.getName()) && hotelRepo.existsByAddress(hotel.getAddress())){
            throw new RuntimeException("Hotel Already Existed");
        }
       return  hotelRepo.save(hotel);
    }

    public Hotel getHotelByCity(String city){
        if(hotelRepo.getHotelByCity(city)==null){
             throw  new RuntimeException("Hotel not Found");
        }
        else{
           return  hotelRepo.getHotelByCity(city);
        }
    }
    public List<Hotel> getAllHotels() {
        return hotelRepo.findAll();
    }

    public void  updateHotel(Long id, Hotel hotel) {

        if(( hotelRepo.findById(id))==null){
          throw   new RuntimeException("Hotel not found");
        }
       Optional<Hotel> existinghotel =hotelRepo.findById(id);
        Hotel existingHotel=existinghotel.get();



        if (hotel.getName() != null && !hotel.getName().isBlank()) {
            existingHotel.setName(hotel.getName());
        }

        if (hotel.getCity() != null && !hotel.getCity().isBlank()) {
            existingHotel.setCity(hotel.getCity());
        }

        if (hotel.getAddress() != null && !hotel.getAddress().isBlank()) {
            existingHotel.setAddress(hotel.getAddress());
        }

        if (hotel.getRating() != null) {
            existingHotel.setRating(hotel.getRating());
        }
         hotelRepo.save(existingHotel);


    }

    @Transactional
    public void deleteHotel(Long id) {

        if (!hotelRepo.existsById(id)) {
            throw new RuntimeException("Hotel not found");

        }

        hotelRepo.deleteById(id);

    }






}
