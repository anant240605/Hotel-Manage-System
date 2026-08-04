package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.DTO.HotelResponse;
import com.HotelBookingSystem.HBS.DTO.RoomResponse;
import com.HotelBookingSystem.HBS.Entity.Hotel;
import com.HotelBookingSystem.HBS.Exception.HotelException;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServices {
    private final HotelRepo hotelRepo;


    @CacheEvict(value = "hotelByCity", allEntries = true)
    public void saveHotel(Hotel hotel) {
        if (hotelRepo.existsByName(hotel.getName()) && hotelRepo.existsByAddress(hotel.getAddress())) {
            throw new HotelException(MessageConstants.HOTEL_ALREADY_EXISTS);
        }

         hotelRepo.save(hotel);
    }



    @Cacheable(value = "hotelByCity",
            key = "#city.toLowerCase().trim()")
    public List<HotelResponse> getHotelByCity(String city) {

        System.out.println("Fetching from db ");

        List<Hotel> hotels = hotelRepo.getHotelByCity(city);
        if(hotels.isEmpty()){
             throw new HotelException(MessageConstants.NO_HOTELS_FOUND);
        }

        return hotels.stream().map(hotel -> {

            List<RoomResponse> rooms = hotel.getRooms()
                    .stream()
                    .map(room -> new RoomResponse(
                            room.getId(),
                            room.getRoomNumber(),
                            room.getPricePerNight(),
                            room.getCapacity(),
                            room.getRoomType(),
                            room.getAvailable()
                    ))
                    .collect(Collectors.toList());

            return new HotelResponse(
                    hotel.getId(),
                    hotel.getName(),
                    hotel.getCity(),
                    hotel.getAddress(),
                    hotel.getRating(),
                    rooms
            );

        }).collect(Collectors.toList());
    }


    public List<Hotel> getAllHotels() {
        return hotelRepo.findAll();
    }

    public void updateHotel(Long id, Hotel hotel) {

        Hotel existingHotel = hotelRepo.findById(id).orElseThrow(()->new RuntimeException(MessageConstants.HOTEL_NOT_FOUND));

        if ( !hotel.getName().isBlank()) {
            existingHotel.setName(hotel.getName());
        }

        if (!hotel.getCity().isBlank()) {
            existingHotel.setCity(hotel.getCity());
        }

        if ( !hotel.getAddress().isBlank()) {
            existingHotel.setAddress(hotel.getAddress());
        }

        if (hotel.getRating() != null) {
            existingHotel.setRating(hotel.getRating());
        }
        hotelRepo.save(existingHotel);


    }

    @CacheEvict(value = "hotelByCity", allEntries = true
    )
    @Transactional
    public void deleteHotel(Long id) {

        if (!hotelRepo.existsById(id)) {
            throw new HotelException(MessageConstants.HOTEL_NOT_FOUND);

        }

        hotelRepo.deleteById(id);

    }
}
