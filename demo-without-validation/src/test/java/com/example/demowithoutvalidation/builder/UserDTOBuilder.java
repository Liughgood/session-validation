package com.example.demowithoutvalidation.builder;

import com.example.demowithoutvalidation.dto.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDTOBuilder {
    private final UserDTO userDTO = new UserDTO();

    public static UserDTOBuilder withDefault() {
        return new UserDTOBuilder()
                .withAge(23)
                .withEmail("12345@168.com")
                .withBirthDay("19991031")
                .withGender(true)
                .withIdentityNumber("123456199910310017")
                .withName("张三");
    }

    public UserDTO build() {
        return userDTO;
    }

    public UserDTOBuilder withName(String name) {
        userDTO.setName(name);
        return this;
    }

    public UserDTOBuilder withAge(Integer age) {
        userDTO.setAge(age);
        return this;
    }

    public UserDTOBuilder withGender(Boolean gender) {
        userDTO.setGender(gender);
        return this;
    }

    public UserDTOBuilder withBirthDay(Instant birthDay) {
        userDTO.setBirthDay(birthDay);
        return this;
    }

    public UserDTOBuilder withBirthDay(String yyyyMMdd) {
        userDTO.setBirthDay(DateTimeFormatter.BASIC_ISO_DATE.parse(yyyyMMdd, LocalDate::from).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return this;
    }

    public UserDTOBuilder withIdentityNumber(String identityNumber) {
        userDTO.setIdentityNumber(identityNumber);
        return this;
    }

    public UserDTOBuilder withEmail(String email) {
        userDTO.setEmail(email);
        return this;
    }

}
