package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);

    List<Booking> findByStatusAndCreatedAtBefore(
            BookingStatus status,
            LocalDateTime createdAt
    );

    List<Booking> findByRoomIdAndStatusIn(
            Long roomId,
            List<BookingStatus> statuses
    );

}
