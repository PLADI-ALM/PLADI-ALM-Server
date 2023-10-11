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
    @Schema(type = "String", description = "직위", example = "팀장", required = true)
    @NotBlank(message = "U0009")
    private String position;
    @Schema(type = "String", description = "직책", example = "마케팅 팀장", required = true)
    @NotBlank(message = "U0010")
    private String officeJob;
    @Schema(type = "String", description = "역할(일반|관리자)", example = "일반", required = true)
    @NotBlank(message = "U0011")
    private String role;
}
