package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPositionRes {
    private String name;
    private String position;

    public static UserPositionRes toDto (User user){
        return UserPositionRes.builder()
                .name(user.getName())
                .position(user.getPosition().getName())
                .build();
    }
}
