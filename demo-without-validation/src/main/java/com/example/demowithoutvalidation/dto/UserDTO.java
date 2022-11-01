package com.example.demowithoutvalidation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserDTO {
    private String name;
    private Integer age;
    private Boolean gender;
    private Instant birthDay;
    private String identityNumber;
    private String email;
}
