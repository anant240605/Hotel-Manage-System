package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.DTO.ReviewRequest;
import com.HotelBookingSystem.HBS.DTO.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(Long hotelId,
                             Long userId,
                             ReviewRequest request);

    List<ReviewResponse> getReviews(Long hotelId);

    Double getAverageRating(Long hotelId);

}