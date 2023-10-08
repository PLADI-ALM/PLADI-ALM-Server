package com.example.pladialmserver.global.feign.event;

import com.example.pladialmserver.global.feign.dto.UserReq;
import com.example.pladialmserver.user.entity.User;
import lombok.Getter;

@Getter
public class DeleteUserEvent {
    private Long userIdx;
    private String nickname;

    public static DeleteUserEvent toEvent(User user){
        DeleteUserEvent userJoinEvent = new DeleteUserEvent();
        userJoinEvent.userIdx = user.getUserId();
        userJoinEvent.nickname = user.getName();
        return userJoinEvent;
    }

    public UserReq toDto() {
        return UserReq.builder()
                .userId(this.userIdx)
                .name(this.nickname)
                .build();
    }
}
