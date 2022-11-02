package com.example.demowithvalidation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @NotNull
    private Boolean gender;
    @NotNull
    private Instant birthDay;
    @NotBlank
    private String identityNumber;
    @NotBlank
    private String email;
    @NotEmpty
    private List<FriendDTO> friendDTOs;
}
