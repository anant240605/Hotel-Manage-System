package com.HotelBookingSystem.HBS.ServiceImpl;

import com.HotelBookingSystem.HBS.DTO.ReviewRequest;
import com.HotelBookingSystem.HBS.DTO.ReviewResponse;
import com.HotelBookingSystem.HBS.Entity.*;
import com.HotelBookingSystem.HBS.Exception.ReviewException;
import com.HotelBookingSystem.HBS.Repository.BookingRepo;
import com.HotelBookingSystem.HBS.Repository.HotelRepo;
import com.HotelBookingSystem.HBS.Repository.ReviewRepo;
import com.HotelBookingSystem.HBS.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final BookingRepo bookingRepo;
    private final HotelRepo hotelRepo;

    @Override
    public ReviewResponse addReview(Long hotelId,
                                    Long userId,
                                    ReviewRequest request) {

        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() ->
                        new ReviewException("Booking not found"));


        if (!booking.getUser().getId().equals(userId)) {
            throw new ReviewException(
                    "This booking doesn't belong to the user");
        }


        Hotel hotel = booking.getRoom().getHotel();

        if (!hotel.getId().equals(hotelId)) {
            throw new ReviewException(
                    "Booking doesn't belong to this hotel");
        }


        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ReviewException(
                    "Only confirmed bookings can be reviewed");
        }


        if (booking.getCheckOutDate().isAfter(LocalDate.now())) {
            throw new ReviewException(
                    "You can review only after checkout");
        }


        if (reviewRepo.existsByBooking(booking)) {
            throw new ReviewException(
                    "Review already submitted");
        }


        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ReviewException(
                    "Rating should be between 1 and 5");
        }

        Review review = new Review();

        review.setRating(request.getRating());

        review.setReview(request.getReview());

        review.setCreatedAt(LocalDateTime.now());

        review.setBooking(booking);

        review.setHotel(hotel);

        review.setUser(booking.getUser());

        reviewRepo.save(review);

        Double avg = reviewRepo.getAverageRating(hotelId);

        hotel.setRating(avg);

        hotelRepo.save(hotel);

        return new ReviewResponse(
                review.getId(),
                review.getUser().getName(),
                review.getRating(),
                review.getReview(),
                review.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponse> getReviews(Long hotelId) {

        List<Review> reviews =
                reviewRepo.findByHotelId(hotelId);

        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getUser().getName(),
                        review.getRating(),
                        review.getReview(),
                        review.getCreatedAt()))
                .toList();
    }

    @Override
    public Double getAverageRating(Long hotelId) {

        Double rating =
                reviewRepo.getAverageRating(hotelId);

        if (rating == null)
            return 0.0;
        return rating;
    }

}