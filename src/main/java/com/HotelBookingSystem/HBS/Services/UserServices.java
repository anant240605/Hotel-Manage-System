package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Constants.MessageConstants;
import com.HotelBookingSystem.HBS.Entity.Role;
import com.HotelBookingSystem.HBS.Entity.User;
import com.HotelBookingSystem.HBS.Exception.UserException;
import com.HotelBookingSystem.HBS.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepo userRepo;

    public void registerUser(User user) {

        if (!userRepo.existsByEmail(user.getEmail())) {
            if (Objects.isNull(user.getRole())) {
                user.setRole(Role.CUSTOMER);
            }

            userRepo.save(user);
        } else {
            throw new UserException(MessageConstants.EMAIL_NOT_FOUND);
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();

    }


    public void deleteUserByEmail(String email) {
        userRepo.deleteByEmail(email);

    }

    public User updateUsersByEmail(User user, String email) {
        if (userRepo.existsByEmail(email)) {

            User olduser = userRepo.getUserByEmail(email);
            if (!StringUtils.hasText(user.getEmail())) {
                olduser.setName(user.getName());
            }


            if (user.getEmail() != null && user.getEmail().isEmpty()) {
                olduser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                olduser.setPassword(user.getPassword());
            }
            if (user.getRole() != null) {
                olduser.setRole(user.getRole());
            }
            return olduser;

        } else {
            return null;
        }

    }

}
