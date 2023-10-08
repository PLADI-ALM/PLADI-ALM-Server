package com.example.pladialmserver.global.feign.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserReq {
    private Long userId;
    private String name;

    @Builder
    public UserReq(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
