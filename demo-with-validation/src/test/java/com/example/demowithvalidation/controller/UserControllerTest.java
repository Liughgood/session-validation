package com.example.demowithvalidation.controller;

import com.example.demowithvalidation.builder.UserDTOBuilder;
import com.example.demowithvalidation.dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Positive;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
class UserControllerTest {
    private static final String UTF_8 = "UTF-8";
    public static final String URL_BINDINGRESULT = "/user/bindingResult";
    public static final String URL_VALIDATOR = "/user/validator";
    public static final String USER_NO_ADULT = "/user/noAdult";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Validator validator;


    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    protected MockMvcRequestSpecification given() {
        return RestAssuredMockMvc
                .given()
                .header("Accept", ContentType.JSON.withCharset(UTF_8))
                .header("Content-Type", ContentType.JSON.withCharset(UTF_8));
    }

    @Test
    void createUserBindingResult() {
        UserDTO userDTO = UserDTOBuilder.withDefault().build();
        MockMvcResponse mvcResponse = given()
                .body(userDTO)
                .post(URL_BINDINGRESULT);
        System.out.println(mvcResponse.getStatusCode());
        mvcResponse.getBody().print();
    }

    @Test
    void createUserValidator() {
        UserDTO userDTO = UserDTOBuilder.withDefault().build();
        MockMvcResponse mvcResponse = given()
                .body(userDTO)
                .post(URL_VALIDATOR);
        System.out.println(mvcResponse.getStatusCode());
        mvcResponse.getBody().print();
    }

    @Test
    void createNoAdultUser() {
        UserDTO userDTO = UserDTOBuilder.withDefault()
                .withAge(18)
                .build();
        MockMvcResponse mvcResponse = given()
                .body(userDTO)
                .post(USER_NO_ADULT);
        System.out.println(mvcResponse.getStatusCode());
        mvcResponse.getBody().print();
    }

    @Test
    void getMoneyTest() throws NoSuchMethodException {
        ExecutableValidator executableValidator = validator.forExecutables();
        Method happy = UserControllerTest.class.getMethod("getMoney", double.class);
        double money = 0.0D;
        Set<ConstraintViolation<Object>> methodViolations = executableValidator.validateParameters(this, happy, new Object[]{money});
        for (ConstraintViolation<Object> violation : methodViolations) {
            System.out.println(violation.getPropertyPath().toString() + violation.getMessage());
        }
    }

    public void getMoney(@Positive double money) {
        System.out.printf("I got $ %.2f.", money);
    }
}
