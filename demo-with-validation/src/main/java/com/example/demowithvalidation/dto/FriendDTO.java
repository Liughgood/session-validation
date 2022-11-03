package com.example.demowithvalidation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FriendDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String hairNumber;
}
