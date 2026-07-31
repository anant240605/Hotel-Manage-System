package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    boolean existsByBooking(Booking booking);

    List<Review> findByHotelId(Long hotelId);

    @Query("""
            SELECT AVG(r.rating)
            FROM Review r
            WHERE r.hotel.id = :hotelId
            """)
    Double getAverageRating(Long hotelId);

}