package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.DTO.ReviewRequest;
import com.HotelBookingSystem.HBS.DTO.ReviewResponse;
import com.HotelBookingSystem.HBS.Services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long hotelId,
            @RequestParam Long userId,
            @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.addReview(hotelId, userId, request)
        );
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(
            @PathVariable Long hotelId) {

        return ResponseEntity.ok(
                reviewService.getReviews(hotelId)
        );
    }


    @GetMapping("/hotel/{hotelId}/rating")
    public ResponseEntity<Double> getRating(
            @PathVariable Long hotelId) {

        return ResponseEntity.ok(
                reviewService.getAverageRating(hotelId)
        );
    }

}