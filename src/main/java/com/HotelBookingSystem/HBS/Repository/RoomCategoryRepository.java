package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.RoomCategory;
import com.HotelBookingSystem.HBS.Entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
    List<RoomCategory> findByHotelId(Long hotelId);
    Optional<RoomCategory> findByHotelIdAndRoomType(Long hotelId, RoomType roomType);
}
