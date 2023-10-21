package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPositionRes {
    @Schema(type = "String", description = "사용자 이름", example = "홍길동")
    private String name;

    public static UserPositionRes toDto (User user){
        return UserPositionRes.builder()
                .name(user.getName())
                .build();
    }
}
