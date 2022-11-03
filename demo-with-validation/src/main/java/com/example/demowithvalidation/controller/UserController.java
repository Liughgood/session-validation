package com.example.demowithvalidation.controller;


import com.example.demowithvalidation.dto.UserDTO;
import com.example.demowithvalidation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final Validator autovalidator;

    @PostMapping("/bindingResult")
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder errorMessages = new StringBuilder();
            allErrors.forEach(errors ->
                errorMessages.append(errors.getDefaultMessage()+lineSeparator())
            );
            return errorMessages.toString();
        }
        return userService.createUser(userDTO);
    }

    @PostMapping("/validator")
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserDTO userDTO){


        Set<ConstraintViolation<UserDTO>> constraintViolations = autovalidator.validate(userDTO);
        StringBuilder errorMessages = new StringBuilder();
        constraintViolations.forEach(error->
            errorMessages.append(error.getMessage()+lineSeparator())
        );
        if(errorMessages.length()>0){
            return errorMessages.toString();
        }
        return userService.createUser(userDTO);
    }

    @PostMapping("/noAdult")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNoAdultUser(@RequestBody @Validated(UserDTO.GroupNoAdult.class) UserDTO userDTO){
        return userService.createUser(userDTO);
    }
}
