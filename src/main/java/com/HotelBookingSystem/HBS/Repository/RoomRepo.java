package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.Room;
import com.HotelBookingSystem.HBS.Entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepo extends JpaRepository<Room, Long > {
   List<Room> findByHotelId(Long hotelId);
   Room findByIdAndHotelId(Long roomId,Long hotelId);
   Optional<Room> findFirstByHotelIdAndRoomTypeAndAvailable(
           Long hotelId,
           RoomType roomType,
           String available
   );

   List<Room> findByHotelIdAndRoomType(Long hotelId, RoomType roomType);
}
