package com.HotelBookingSystem.HBS.Services;

import com.HotelBookingSystem.HBS.Entity.Role;
import com.HotelBookingSystem.HBS.Entity.User;
import com.HotelBookingSystem.HBS.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Objects;


@Service
public class UserServices {
    @Autowired
    private UserRepo userRepo;

    public User registerUser(User user){

            if(!userRepo.existsByEmail(user.getEmail())){
                if(Objects.isNull(user.getRole())) {
                    user.setRole(Role.CUSTOMER);
                }

                return userRepo.save(user);
            }else{
                  throw new RuntimeException("Email Already Existed");
            }
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();

    }


    public boolean deleteUserByEmail( String email){
         userRepo.deleteByEmail(email);
         return true;
    }

    public User updateUsersByEmail(User user, String email){
        if(userRepo.existsByEmail(email)){

            User olduser= userRepo.getUserByEmail(email);
            if(!StringUtils.isEmpty(user.getEmail())){
                olduser.setName(user.getName());
            }


            if(user.getEmail()!=null && user.getEmail()!=""){
                olduser.setEmail(user.getEmail());
            }
            if(user.getPassword()!=null && !user.getPassword().equals("")){
                olduser.setPassword(user.getPassword());
            }
            if(user.getRole()!=null){
                olduser.setRole(user.getRole());
            }
            return olduser;

        }else{
            return null;
        }

    }

}
