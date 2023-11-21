package com.example.pladialmserver.user.dto;

import com.example.pladialmserver.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    @Schema(type = "String", description = "AccessToken", example = "bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTY3NTg2OTcsInVzZXJJZHgiOjEsInN1YiI6IjEifQ.DSBuBlStkjhT05vuzjWd-cg7naG5KikUxII734u3nUw")
    @NotBlank(message = "G0001")
    private String accessToken;
    @NotBlank(message = "G0001")
    @Schema(type = "String", description = "RefreshToken", example = "bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTY3NTg2OTcsInVzZXJJZHgiOjEsInN1YiI6IjEifQ.DSBuBlStkjhT05vuzjWd-cg7naG5KikUxII734u3nUw")
    private String refreshToken;
    @Schema(type = "String", description = "사용자 역할", example = "BASIC / ADMIN")
    private Role role;

    public static TokenDto toDto(String accessToken, String refreshToken, Role role){
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
}
