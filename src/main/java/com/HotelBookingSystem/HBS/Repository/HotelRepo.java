package com.HotelBookingSystem.HBS.Repository;


import com.HotelBookingSystem.HBS.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepo extends JpaRepository<Hotel, Long> {
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    Hotel getHotelByCity(String city);
    Optional<Hotel> findByName(String name);
}
