package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);

}
