package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AdminResponsibilityListRes {
    @Schema(type = "Long", description = "자원 Id", example = "1")
    private List<AdminResponsibilityRes> responsibilityList;

    public static AdminResponsibilityListRes toDto(List<User> users){
        return AdminResponsibilityListRes.builder()
                .responsibilityList(users.stream().map(AdminResponsibilityRes::toDto).collect(Collectors.toList()))
                .build();
    }
}
