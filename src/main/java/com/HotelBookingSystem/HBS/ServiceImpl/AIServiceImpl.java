package com.HotelBookingSystem.HBS.ServiceImpl;
import com.HotelBookingSystem.HBS.DTO.*;
import com.HotelBookingSystem.HBS.Entity.AIIntent;
import com.HotelBookingSystem.HBS.Entity.Booking;
import com.HotelBookingSystem.HBS.Entity.PaymentMethod;
import com.HotelBookingSystem.HBS.Services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
private  final HotelServices hotelServices;
private final ChatClient chatClient;
    private final ConversationMemoryService conversationMemoryService;
    private final ObjectMapper objectMapper;
    private final BookingServices bookingServices;
    private final PaymentServices paymentServices;

    @Override
    public String chat(String sessionId, Long userId, String prompt) {
        ConversationContext contextt =
                conversationMemoryService.get(sessionId);

        AIIntent intent;

        if (contextt != null
                && contextt.isWaitingForPayment()
                && prompt.equalsIgnoreCase("YES")) {

            intent = AIIntent.PAYMENT_CONFIRM;

        }else if(contextt != null
                &&
                contextt.isWaitingForPayment()
                &&
                contextt.getPaymentMethod()==null){

            return handlePaymentMethod(
                    sessionId,
                    prompt);

        }
        else {

            intent = detectIntent(prompt);

        }

        switch (intent) {

            case SEARCH_HOTEL:

                String city = extractCity(prompt);

                SearchHotelResult result = searchHotels(city);

                ConversationContext context = new ConversationContext();
                context.setUserId(userId);

                context.setSearchResult(result);

                conversationMemoryService.save(sessionId, context);

                return buildHotelResponse(result);

            case BOOK_HOTEL:

                return handleBooking(sessionId, prompt);

            case BOOKING_DETAILS:

                return handleBookingDetails(
                        sessionId,
                        prompt
                );
            case CONFIRM_BOOKING:

                return confirmBooking(sessionId);
            case PAYMENT_CONFIRM:

                return handlePaymentConfirmation(sessionId);

            default:

                return "Sorry, I didn't understand your request.";

        }
    }

    private String extractCity(String prompt) {

        String systemPrompt = """
            You are an AI assistant.

            Extract ONLY the city name.

            Return only the city name.
            """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .content();

    }
    private int extractHotelNumber(String prompt){
        String systemPrompt = """
You are an AI assistant.

Extract ONLY the hotel number.

Examples

Book first hotel
Output:
1

Book second hotel
Output:
2

Book third hotel
Output:
3

Return ONLY the number.
""";
        String response =
                chatClient.prompt()
                        .system(systemPrompt)
                        .user(prompt)
                        .call()
                        .content()
                        .trim();

        System.out.println(response);
        return Integer.parseInt(response);
    }


    private SearchHotelResult searchHotels(
            String city){

        List<HotelResponse> hotels =
                hotelServices.getHotelByCity(city);

        return new SearchHotelResult(
                city,
                hotels);

    }

    private String buildHotelResponse(
            SearchHotelResult result){

        StringBuilder builder =
                new StringBuilder();

        builder.append("Hotels found in ")
                .append(result.getCity())
                .append("\n\n");
  int index=1;
        for(HotelResponse hotel :
                result.getHotels()){
            builder.append(index++)
                    .append(". ")
                    .append(hotel.getName())
                    .append(" ⭐ ")
                    .append(String.format("%.1f",
                            hotel.getRating()))
                    .append("\n");

            builder.append("Rating : ")
                    .append(String.format("%.1f",
                            hotel.getRating()))
                    .append("\n");

            builder.append("------------------\n");

        }

        return builder.toString();

    }

    private AIIntent detectIntent(String prompt){String systemPrompt = """
You are an AI intent classifier.

Return ONLY one value.

SEARCH_HOTEL
BOOK_HOTEL
BOOKING_DETAILS
PAYMENT
UNKNOWN

Rules:

If user is searching hotels
→ SEARCH_HOTEL

If user selects a hotel
(Book first hotel,
Book second hotel)
→ BOOK_HOTEL

If user provides room type,
check in,
check out,
dates,
number of guests
→ BOOKING_DETAILS
If user replies

YES

CONFIRM

BOOK NOW

GO AHEAD

CONFIRM BOOKING

Return

CONFIRM_BOOKING

If the user wants to proceed with payment
or says

YES

PAY NOW

CONTINUE PAYMENT

Return

PAYMENT_CONFIRM

Otherwise
→ UNKNOWN

Return ONLY one word.
""";


        String response =
                chatClient.prompt()

                        .system(systemPrompt)

                        .user(prompt)

                        .call()

                        .content()

                        .trim();

        return AIIntent.valueOf(response);

    }

    private String handleBooking(
            String sessionId,
            String prompt) {

        ConversationContext context =
                conversationMemoryService.get(sessionId);

        if (context == null) {

            return "Please search hotels first.";

        }

        int hotelNumber = extractHotelNumber(prompt);

        HotelResponse selectedHotel =
                context.getSearchResult()
                        .getHotels()
                        .get(hotelNumber - 1);

        context.setSelectedHotel(selectedHotel);

        conversationMemoryService.save(sessionId, context);

        return """
You selected:

%s

Please tell me:

1. Room Type

2. Check-In Date

3. Check-Out Date
""".formatted(selectedHotel.getName());

    }

    private String handleBookingDetails(
            String sessionId,
            String prompt){

        ConversationContext context =
                conversationMemoryService.get(sessionId);

        if(context == null){

            return "Please search hotels first.";

        }

        BookingDetails details =
                extractBookingDetails(prompt);

        context.setRoomType(
                details.getRoomType());

        context.setCheckIn(
                details.getCheckIn());

        context.setCheckOut(
                details.getCheckOut());
        if(context.getSelectedHotel()==null){

            return """
           Please select a hotel first.

           Example:

           Book first hotel
           """;

        }
        if(details.getRoomType()==null){

            return "Please provide room type.";

        }
        if(details.getCheckIn()==null
                ||
                details.getCheckOut()==null){

            return "Please provide check-in and check-out dates.";

        }
        if(!details.getCheckOut()
                .isAfter(details.getCheckIn())){

            return """
           Check-out date must be after
           check-in date.
           """;

        }
        if(details.getCheckIn()
                .isBefore(LocalDate.now())){

            return "Check-in date cannot be in the past.";

        }
        context.setRoomType(
                details.getRoomType());

        context.setCheckIn(
                details.getCheckIn());

        context.setCheckOut(
                details.getCheckOut());

        conversationMemoryService.save(
                sessionId,
                context);

        return """
            Booking Details Saved Successfully.

            Hotel : %s

            Room Type : %s

            Check In : %s

            Check Out : %s
          
                Reply YES to confirm booking.
            """
                .formatted(

                        context
                                .getSelectedHotel()
                                .getName(),

                        details
                                .getRoomType(),

                        details
                                .getCheckIn(),

                        details
                                .getCheckOut()

                );

    }

    private BookingDetails extractBookingDetails(String prompt){
        String systemPrompt = """
You are an AI assistant.

Extract booking details from the user's message.

Return ONLY valid JSON.

Format:

{
  "roomType":"DELUXE",
  "checkIn":"2026-08-10",
  "checkOut":"2026-08-12"
}

Rules:

1. roomType must be STANDARD, DELUXE or SUITE.
2. Dates must be yyyy-MM-dd.
3. Return only JSON.
""";

        String response =
                chatClient.prompt()
                        .system(systemPrompt)
                        .user(prompt)
                        .call()
                        .content()
                        .trim();

        System.out.println(response);

        try {


            return objectMapper.readValue(
                    response,
                    BookingDetails.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to parse booking details"
            );

        }

    }

    private String confirmBooking(
            String sessionId){

        ConversationContext context =
                conversationMemoryService.get(sessionId);

        if(context == null){

            return "Session Expired. Please search hotels again.";

        }
        if(context.getSelectedHotel()==null){

            return "No hotel selected.";

        }

        if(context.getRoomType()==null){

            return "Room type missing.";

        }

        if(context.getCheckIn()==null
                ||
                context.getCheckOut()==null){

            return "Booking details missing.";

        }

        BookingRequest request =
                new BookingRequest(context.getUserId(),

                        context.getSelectedHotel().getName(),

                        context.getRoomType(),

                        context.getCheckIn(),

                        context.getCheckOut());

        Booking booking =
                bookingServices.createBooking(request);

        context.setBookingId(booking.getId());

        context.setWaitingForPayment(true);

        conversationMemoryService.save(sessionId, context);


        return """
Booking Created Successfully.

Booking Id : %d

Status : %s

Would you like to pay now?

Reply YES or NO.
"""
                .formatted(

                        booking.getId(),

                        booking.getStatus()

                );


    }

    private PaymentMethod detectPaymentMethod(String prompt){

        String systemPrompt = """
You are an AI assistant.

Return ONLY one value.

UPI

CARD

NET_BANKING

UNKNOWN
""";

        String response =
                chatClient.prompt()
                        .system(systemPrompt)
                        .user(prompt)
                        .call()
                        .content()
                        .trim();

        return PaymentMethod.valueOf(response);

    }

    private String handlePaymentConfirmation(
            String sessionId){



        return """
Choose Payment Method

1. UPI

2. CARD

3. NET BANKING
""";

    }

    private String handlePaymentMethod(
            String sessionId,
            String prompt){
        ConversationContext context = conversationMemoryService.get(sessionId);
        PaymentMethod paymentMethod = detectPaymentMethod(prompt);
        context.setPaymentMethod(paymentMethod);

        conversationMemoryService.save(sessionId, context);

        PaymentRequest request = new PaymentRequest();

        request.setBookingId(
                context.getBookingId());
        request.setPaymentMethod(
                paymentMethod);
        paymentServices.makePayment(request);
        conversationMemoryService.delete(sessionId);
        return """
Payment Successful.

Booking Confirmed.

Payment Method : %s

Confirmation email has been sent.

Thank you for choosing our service.
"""
                .formatted(paymentMethod);
    }




}
