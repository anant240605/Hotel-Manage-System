package com.HotelBookingSystem.HBS.Repository;

import com.HotelBookingSystem.HBS.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    User getUserByEmail(String email);
}
