package com.example.pladialmserver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserReq {
    @Schema(type = "String", description = "성명", example = "홍길동", required = true)
    @NotBlank(message = "U0007")
    private String name;
    @Schema(type = "String", description = "휴대폰", example = "010-0000-0000", required = true)
    @Pattern(message = "U0010", regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    @NotBlank(message = "U0009")
    private String phone;
    @Schema(type = "String", description = "자산(컴퓨터, 태블릿)", example = "A123434, B123434")
    private String asserts;

    @Builder
    public UpdateUserReq(String name, String phone, String asserts) {
        this.name = name;
        this.phone = phone;
        this.asserts = asserts;
    }

    public static UpdateUserReq  toDto(AdminUpdateUserReq req){
        return UpdateUserReq.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .asserts(req.getAsserts())
                .build();
    }

}
