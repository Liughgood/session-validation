package com.example.demowithoutvalidation.service;

import com.example.demowithoutvalidation.Exceptions.UserException;
import com.example.demowithoutvalidation.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {

    public static final String IDENTITY_NUMBER_REG = "(\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d[Xx])$)";
    public static final String EMAIL_REG = "^([a-zA-Z0-9_-])+@[a-zA-Z0-9_-]{2,50}+(.[a-zA-Z]{2,3})$";

    public String createUser(UserDTO userDTO) {
        if (!(userDTO.getName().length() >= 2 && userDTO.getName().length() <= 4)) {
            throw new UserException("名字不为空且必须在 2 到 4 个字之间");
        }
        if (Objects.isNull(userDTO.getAge())) {
            throw new UserException("年龄不能为空");
        }
        if (!(userDTO.getAge() >= 18 && userDTO.getAge() <= 35)) {
            throw new UserException("年龄必须在 18 到 35 之间");
        }
        if (Objects.isNull(userDTO.getGender())) {
            throw new UserException("性别不能为空");
        }
        if (Objects.isNull(userDTO.getBirthDay())) {
            throw new UserException("生日不能为空");
        }
        if (!userDTO.getBirthDay().isBefore(Instant.now())) {
            throw new UserException("生日必须是过去的日期");
        }
        if (userDTO.getIdentityNumber().isEmpty()) {
            throw new UserException("身份证号不能为空");
        }
        if (!isAvailableString(userDTO.getIdentityNumber(), IDENTITY_NUMBER_REG)) {
            throw new UserException("身份证号不符合格式");
        }
        if (userDTO.getEmail().isEmpty()) {
            throw new UserException("Email不能为空");
        }
        if (!isAvailableString(userDTO.getEmail(), EMAIL_REG)) {
            throw new UserException("Email不符合格式");
        }
        if (!identityNumberBeConsistentWithBirthdayAndGender(userDTO.getIdentityNumber(), userDTO.getBirthDay(), userDTO.getGender())) {
            throw new UserException("身份证号与生日、性别不符");
        }

        // add your business...
        return "created";
    }

    private boolean isAvailableString(String myString, String reg) {

        return Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(myString).matches();
    }

    private boolean identityNumberBeConsistentWithBirthdayAndGender(String identityNumber, Instant birthday, Boolean gender) {
        String birthdayStr = identityNumber.substring(6, 14);
        Instant idBirthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Boolean idGender = Integer.parseInt(identityNumber.substring(16, 17)) % 2 != 0;
        return (idBirthday.compareTo(birthday) == 0 && gender.equals(idGender));
    }
}
