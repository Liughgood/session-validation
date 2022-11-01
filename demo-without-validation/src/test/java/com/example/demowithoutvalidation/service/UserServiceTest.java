package com.example.demowithoutvalidation.service;

import com.example.demowithoutvalidation.builder.UserDTOBuilder;
import com.example.demowithoutvalidation.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
class UserServiceTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserService userService;

    @Test
    void createUser() {
        UserDTO userDTO = UserDTOBuilder.withDefault().build();
        userService.createUser(userDTO);
    }
}
