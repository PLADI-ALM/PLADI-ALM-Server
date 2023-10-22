package com.example.pladialmserver.user.dto.request;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ResponsibilityListRes {
    @Schema(type = "Arrays", description = "책임자 리스트")
    private List<ResponsibilityRes> responsibilityList;

    public static ResponsibilityListRes toDto(List<User> users){
        return ResponsibilityListRes.builder()
                .responsibilityList(users.stream().map(ResponsibilityRes::toDto).collect(Collectors.toList()))
                .build();
    }
}
