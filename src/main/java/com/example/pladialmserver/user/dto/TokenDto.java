package com.example.pladialmserver.user.dto;

import com.example.pladialmserver.user.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Role role;

    public static TokenDto toDto(String accessToken, String refreshToken, Role role){
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
}
