# session-validation的使用
## 1. validation 基础使用——bean验证
### 1.传统的验证方式
假设我们有这样一个[UserDTO](demo-without-validation/src/main/java/com/example/demowithoutvalidation/dto/UserDTO.java)。并且提出了如下需求：
1. 这个对象的所有属性不能为空。
2. 名字必须在 2 到 4 个字之间。
3. 年龄必须在 18 到 35 之间。
4. 性别只有生理性别(男|女)。
5. 生日必须是过去的日期。
6. 身份证号必须符合格式（/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/）。
7. 身份证号上生日和性别段必须与上边填写的对应。
8. email 必须符合格式（/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-]{2,})+(.[a-zA-Z]{2,3})$/）。
```java
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
```
然后我们要根据这个DTO来创建新用户，把对象中的信息存入到数据库中。但是在存之前，需要对其校验是否符合我们上面对需求。

在用传统对方式来验证，就需要写很多的判断，非常麻烦；并且这些限制写在service中，当另一个人使用这个DTO中可能看不到这些限制。

例如[UserService](demo-without-validation/src/main/java/com/example/demowithoutvalidation/service/UserService.java)，手动校验非常繁琐，很麻烦。
```java line-numbers
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

    public static final String IDENTITY_NUMBER_REG = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    public static final String EMAIL_REG = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-]{2,})+(.[a-zA-Z]{2,3})$";

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
```
这个时候，Bean validation就派上用场了，可以优雅简洁的完成任务。接下来我们就来尝试将这一系列if都替换掉。
### 2. JSR 380 与依赖安装

首先我们简单介绍一下技术背景。之前我们可以用各种方法去校验java bean，但是大家没有一个统一的规范。后来随着发展，大家总结出来一些标准，最开始作为 JSR 303 规范，后来进行了拓展，叫做 [JSR 380](https://jcp.org/en/jsr/detail?id=380)。

要使用这个，我们先安装相关的依赖。
第一个是[javax.validation:validation-api](https://mvnrepository.com/artifact/javax.validation/validation-api)，这个只是一个接口，并不包含实现，实际上也不用安装。
```
// https://mvnrepository.com/artifact/javax.validation/validation-api
implementation 'javax.validation:validation-api:2.0.1.Final'
```
但是注意的是，这个依赖2.0.1以后的版本，已经迁移到[jakarta.validation](https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api)了，如果在其他项目看到名字不一样，不要感到奇怪。由于目前项目用的2.0.1 final的版本，这个session也是用这个。

第二个是他的实现, [org.hibernate.validator:hibernate-validator](https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator/6.1.5.Final)，它会自己安装好上边的依赖，这边版本也是我们当前项目用的。
```
// https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator
implementation 'org.hibernate.validator:hibernate-validator:6.1.6.Final'
```

这又是啥？
```
// https://mvnrepository.com/artifact/jakarta.el/jakarta.el-api
implementation 'jakarta.el:jakarta.el-api:5.0.1'
```

### 3. 使用注解校验
1. 这个对象的所有属性不能为空。

我首先完成这个校验，属性不能为空，这样我想到了三个很像但是又有区别的注解。
```java
// @NotBlank
// 注解修饰的元素不能为null，或者至少一个非空字符，支持CharSequence
// @NotEmpty
// 注解修饰的元素不能为null，或者空，支持的类型有CharSequence、Collection、Map、Array
// @NotNull
// 注解修饰的元素不能为null，支持任何类型。
// TODO 再研究
// @Deprecated
```
所以我们可以确定给这个类中成员变量加什么注解了。
```java
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

```
2. 名字必须在 2 到 4 个字之间。

校验这个，我们可以使用
