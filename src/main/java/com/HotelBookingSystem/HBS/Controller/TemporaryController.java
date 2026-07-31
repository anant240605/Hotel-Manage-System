package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.Services.RedisLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("lock")
public class TemporaryController {
    @Autowired
    private final RedisLockService lockService;


    @GetMapping("/{roomId}")
    public String lock(@PathVariable Long roomId){

        boolean success =
                lockService.acquireLock(
                        "room:"+roomId
                );

        return success ?
                "Lock Acquired" :
                "Already Locked";
    }
}
