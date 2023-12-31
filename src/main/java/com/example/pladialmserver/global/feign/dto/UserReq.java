package com.example.pladialmserver.global.feign.dto;

import com.example.pladialmserver.user.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class UserReq {
    private Long userId;
    private String name;
    private Role role;

    @Builder
    public UserReq(Long userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
