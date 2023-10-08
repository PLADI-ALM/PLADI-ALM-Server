package com.example.pladialmserver.user.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginReq {
    @Email(message = "U0002")
    @NotBlank(message = "U0004")
    private String email;
    @Pattern(message = "U0003", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$")
    @NotBlank(message = "U0005")
    private String password;
}
