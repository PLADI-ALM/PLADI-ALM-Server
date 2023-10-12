package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyRankRes {
    @Schema(type = "String", description = "직책/부서", example = "마케팅 | 팀장")
    private String name;

    public static CompanyRankRes toDto(Department department){
        return CompanyRankRes.createDto(department.getName());
    }

    public static CompanyRankRes toDto(Position position){
        return CompanyRankRes.createDto(position.getName());
    }

    public static CompanyRankRes createDto(String name){
        return CompanyRankRes.builder()
                .name(name)
                .build();
    }
}
