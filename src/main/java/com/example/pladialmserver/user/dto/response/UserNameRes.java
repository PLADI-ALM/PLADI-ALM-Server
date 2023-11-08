package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNameRes {
    @Schema(type = "String", description = "사용자 이름", example = "홍길동")
    private String name;
    @Schema(type = "String", description = "역할(일반|관리자)", example = "일반")
    private String role;

    public static UserNameRes toDto (User user){
        return UserNameRes.builder()
                .name(user.getName())
                .role(user.getRole().getValue())
                .build();
    }
}
