package com.example.pladialmserver.global.feign.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserReq {
    private Long userId;
    private String name;
    private String role;

    @Builder
    public UpdateUserReq(Long userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
