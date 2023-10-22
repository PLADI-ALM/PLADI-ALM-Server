package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponsibilityRes {
    @Schema(type = "Long", description = "사용자 Id", example = "1")
    private Long userId;
    @Schema(type = "String", description = "사용자 이름 및 부서", example = "홍길동 (마케팅)")
    private String name;

    public static AdminResponsibilityRes toDto(User user){
        return AdminResponsibilityRes.builder()
                .userId(user.getUserId())
                .name(user.getName() + " (" + user.getDepartment().getName() + ")")
                .build();
    }
}
