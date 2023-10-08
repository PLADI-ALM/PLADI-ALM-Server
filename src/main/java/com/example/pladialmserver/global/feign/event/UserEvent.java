package com.example.pladialmserver.global.feign.event;

import com.example.pladialmserver.global.feign.dto.UserReq;
import com.example.pladialmserver.user.entity.User;
import lombok.Getter;

@Getter
public class UserEvent {
    private Long userId;
    private String name;

    public static UserEvent toEvent(User user){
        UserEvent userJoinEvent = new UserEvent();
        userJoinEvent.userId = user.getUserId();
        userJoinEvent.name = user.getName();
        return userJoinEvent;
    }

    public UserReq toDto() {
        return UserReq.builder()
                .userId(this.userId)
                .name(this.name)
                .build();
    }
}
