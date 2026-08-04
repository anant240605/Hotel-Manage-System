package com.HotelBookingSystem.HBS.Controller;

import com.HotelBookingSystem.HBS.Entity.User;
import com.HotelBookingSystem.HBS.Repository.UserRepo;
import com.HotelBookingSystem.HBS.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private  final UserRepo userRepo;

    private final UserServices userServices;

    @PostMapping("/create-user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

            userServices.registerUser(user);
            return new ResponseEntity<>("User Registered Successfully", HttpStatus.CREATED);


    }

    @GetMapping
    public List<User> getAllUsers() {
        return userServices.getAllUsers();
    }

    @Transactional
    @DeleteMapping("delete/{email}")
    public ResponseEntity<?> deleteUsersByEmail(@PathVariable String email) {
        if (!userRepo.existsByEmail(email)) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        userServices.deleteUserByEmail(email);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping("update/{email}")
    public ResponseEntity<?> updateUsersByEmail(@PathVariable String email, @RequestBody User user) {
        try {
            User newUser = userServices.updateUsersByEmail(user, email);
            userServices.registerUser(newUser);
            return new ResponseEntity<>("User updated successfully", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("User not Found", HttpStatus.NOT_FOUND);
        }

    }


}
