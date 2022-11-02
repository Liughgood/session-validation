package com.example.demowithvalidation.service;

import com.example.demowithvalidation.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String createUser(UserDTO userDTO) {
        // add your business...
        return "created";
    }
}
