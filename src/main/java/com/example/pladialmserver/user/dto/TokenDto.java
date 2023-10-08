package com.example.pladialmserver.user.dto;

import com.example.pladialmserver.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    @Schema(type = "String", description = "AccessToken", example = "bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTY3NTg2OTcsInVzZXJJZHgiOjEsInN1YiI6IjEifQ.DSBuBlStkjhT05vuzjWd-cg7naG5KikUxII734u3nUw")
    private String accessToken;
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
