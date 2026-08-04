package com.HotelBookingSystem.HBS.Scheduler;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {


    private final BookingRepo bookingRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatusAndCreatedAtBefore(
                        BookingStatus.PENDING,
                        LocalDateTime.now().minusMinutes(5));
        for (Booking booking : bookings) {
            booking.setStatus(BookingStatus.CANCELLED);
        }
        bookingRepository.saveAll(bookings);

    }

}