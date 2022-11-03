package com.example.demowithvalidation.dto;

import lombok.Getter;
import lombok.Setter;

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
import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "名字不能为空")
    @Size(min = 2, max = 4, message = "名字必须在 2 到 4 个字之间")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 35, message = "年龄不能大于35岁")
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
    private List<FriendDTO> friendDTOs;
}
