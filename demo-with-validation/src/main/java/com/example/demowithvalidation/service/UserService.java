package com.example.demowithvalidation.service;

import com.example.demowithvalidation.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String createUser(UserDTO userDTO) {

        System.out.println(userDTO.getName());
        // add your business...
        return "created";
    }
}
