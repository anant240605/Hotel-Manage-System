package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);

    Room findByIdAndHotelId(Long roomId, Long hotelId);
    long countByRoomCategoryId(Long categoryId);
    List<Room> findByRoomCategoryId(Long roomCategoryId);
}
