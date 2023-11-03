package com.example.pladialmserver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateUserReq {
    @Schema(type = "String", description = "성명", example = "홍길동", required = true)
    @NotBlank(message = "U0007")
    private String name;
    @Schema(type = "String", description = "이메일", example = "1234@email.com", required = true)
    @Email(message = "U0002")
    @NotBlank(message = "U0004")
    private String email;
    @Schema(type = "String", description = "비밀번호", example = "qwer1234!", required = true)
    @Pattern(message = "U0003", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$")
    @NotBlank(message = "U0005")
    private String password;
    @Schema(type = "String", description = "부서", example = "마케팅", required = true)
    @NotBlank(message = "U0008")
    private String department;
    @Schema(type = "String", description = "휴대폰", example = "010-0000-0000", required = true)
    @Pattern(message = "U0010", regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    @NotBlank(message = "U0009")
    private String phone;
    @Schema(type = "String", description = "역할(일반|관리자)", example = "일반", required = true)
    @NotBlank(message = "U0011")
    private String role;
    @Schema(type = "String", description = "푸쉬알림 토큰", example = "{fcm token}", required = false)
    @NotBlank(message = "T0001")
    private String fcmToken;
}
