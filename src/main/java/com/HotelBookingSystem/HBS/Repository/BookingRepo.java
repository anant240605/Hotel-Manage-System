package com.HotelBookingSystem.HBS.Repository;


import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long > {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByStatusAndCreatedAtBefore(
            BookingStatus status,
            LocalDateTime createdAt
    );

}
