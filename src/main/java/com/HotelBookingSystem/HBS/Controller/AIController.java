package com.HotelBookingSystem.HBS.Controller;
import com.HotelBookingSystem.HBS.DTO.AIChatRequest;
import com.HotelBookingSystem.HBS.DTO.AIChatResponse;
import com.HotelBookingSystem.HBS.Services.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/chat")
    public AIChatResponse chat(@RequestBody AIChatRequest request){

        return new AIChatResponse(

                request.getSessionId(),

                aiService.chat(
                        request.getSessionId(),
                        request.getUserId(),


                        request.getMessage()

                )

        );
    }

}