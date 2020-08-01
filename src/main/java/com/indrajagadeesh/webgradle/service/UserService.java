package com.indrajagadeesh.webgradle.service;

import com.indrajagadeesh.webgradle.model.User;
import com.indrajagadeesh.webgradle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Database interaction service
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String saveUser(User user){
        String result = "";
        try {
            userRepository.save(user);
            result = "User added successfully";
        }catch (Exception e){
            e.printStackTrace();
            result = "error added user";
        }
        return result;
    }
     public List<User> getAllUsers(){
         final List<User> users = new ArrayList<>();
         userRepository.findAll().forEach(users::add);
         return users;
     }

     public String deleteUserbyId(String username){
         String result = "";
        try {
            userRepository.deleteById(username);
            result = "user deleted";
        }catch (Exception e){
            e.printStackTrace();
            result = "error deleting user";
        }
        return result;
     }

     public Optional<User> getUserById(String username){
        return userRepository.findById(username);
     }
}
