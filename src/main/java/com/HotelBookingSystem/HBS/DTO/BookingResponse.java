package com.HotelBookingSystem.HBS.DTO;

import com.HotelBookingSystem.HBS.Entity.BookingStatus;
import com.HotelBookingSystem.HBS.Entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String hotelName;
    private Integer roomNumber;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalPrice;
    private BookingStatus status;
}

