package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CompanyRankListRes {
    @Schema(type = "Arrays", description = "부서 리스트")
    private List<CompanyRankRes> departmentList;
    @Schema(type = "Arrays", description = "직책 리스트")
    private List<CompanyRankRes> positionList;

    public static CompanyRankListRes toDto(List<Department> departments, List<Position> positions){
        return CompanyRankListRes.builder()
                .departmentList(departments.stream().map(CompanyRankRes::toDto).collect(Collectors.toList()))
                .positionList(positions.stream().map(CompanyRankRes::toDto).collect(Collectors.toList()))
                .build();
    }
}
