package com.example.demowithvalidation.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ScriptAssert.List({
        @ScriptAssert(
                lang = "javascript",
                script = "com.example.demowithvalidation.dto.UserDTO.identityNumberBeConsistentWithBirthdayAndGender(_this.identityNumber, _this.birthDay, _this.gender)",
                message = "身份证号与生日、性别不符"),
        @ScriptAssert(lang = "javascript",
                script = "_this.age + java.time.LocalDate.ofInstant(_this.birthDay, java.time.ZoneId.systemDefault()).getYear() == java.time.LocalDate.now().getYear()",
                message = "生日与年龄不符")
})

public class UserDTO {
    @NotBlank(message = "名字不能为空")
    @Size(min = 2, max = 4, message = "名字必须在 2 到 4 个字之间")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18岁", groups = {GroupAdult.class})
    @Max(value = 35, message = "年龄不能大于35岁", groups = {GroupAdult.class})
    @Min(value = 7, message = "年龄不能小于7岁", groups = {GroupNoAdult.class})
    @Max(value = 17, message = "年龄不能大于17岁", groups = {GroupNoAdult.class})
    private Integer age;
    @NotNull(message = "性别不能为空")
    private Boolean gender;
    @NotNull(message = "生日不能为空")
    @Past(message = "生日必须过去的日期")
    private Instant birthDay;
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "(\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d[Xx])$)", message = "身份证号不符合格式")
    private String identityNumber;

    @NotBlank(message = "email不能为空")
    @Email(regexp = "^([a-zA-Z0-9_-])+@[a-zA-Z0-9_-]{2,50}+(.[a-zA-Z]{2,3})$", message = "email不符合格式")
    private String email;

    @NotEmpty(message = "不能没有朋友")
    private List<@Valid FriendDTO> friendDTOs;

    @DecimalMax(value = "99.99", message = "财不外露")
    @DecimalMin(value = "9.99", message = "不能这么穷")
    @Digits(integer = 2, fraction = 2)
    private String money;

    private List<@NotBlank String> justStrings;

    public static boolean identityNumberBeConsistentWithBirthdayAndGender(String identityNumber, Instant birthday, Boolean gender) {
        String birthdayStr = identityNumber.substring(6, 14);
        Instant idBirthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Boolean idGender = Integer.parseInt(identityNumber.substring(16, 17)) % 2 != 0;
        return (idBirthday.compareTo(birthday) == 0 && gender.equals(idGender));
    }

    public interface GroupAdult {

    }

    public interface GroupNoAdult {

    }
}
