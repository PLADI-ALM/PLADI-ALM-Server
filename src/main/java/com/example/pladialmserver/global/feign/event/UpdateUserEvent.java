package com.example.pladialmserver.global.feign.event;

import com.example.pladialmserver.global.feign.dto.UpdateUserReq;
import com.example.pladialmserver.global.feign.dto.UserReq;
import com.example.pladialmserver.user.entity.User;
import lombok.Getter;

@Getter
public class UpdateUserEvent {
    private Long userId;
    private String name;
    private String role;

    public static UpdateUserEvent toEvent(User user){
        UpdateUserEvent userJoinEvent = new UpdateUserEvent();
        userJoinEvent.userId = user.getUserId();
        userJoinEvent.name = user.getName();
        return userJoinEvent;
    }

    public UpdateUserReq toDto() {
        return UpdateUserReq.builder()
                .userId(this.userId)
                .name(this.name)
                .role(this.role)
                .build();
    }
}
