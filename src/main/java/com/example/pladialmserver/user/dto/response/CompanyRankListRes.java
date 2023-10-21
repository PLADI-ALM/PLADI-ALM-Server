package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CompanyRankListRes {
    @Schema(type = "Arrays", description = "부서 리스트")
    private List<String> departmentList;

    public static CompanyRankListRes toDto(List<Department> departments){
        return CompanyRankListRes.builder()
                .departmentList(departments.stream().map(Department::getName).collect(Collectors.toList()))
                .build();
    }
}
