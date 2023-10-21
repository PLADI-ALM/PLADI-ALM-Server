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

    public static UserNameRes toDto (User user){
        return UserNameRes.builder()
                .name(user.getName())
                .build();
    }
}
