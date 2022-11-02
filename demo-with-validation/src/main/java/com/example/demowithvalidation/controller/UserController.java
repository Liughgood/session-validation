package com.example.demowithvalidation.controller;


import com.example.demowithvalidation.dto.UserDTO;
import com.example.demowithvalidation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

}
