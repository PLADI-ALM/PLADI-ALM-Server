package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class DepartmentListDto {
    @Schema(type = "Arrays", description = "부서 리스트")
    private List<String> departmentList;

    public static DepartmentListDto toDto(List<Department> departments){
        return DepartmentListDto.builder()
                .departmentList(departments.stream().map(Department::getName).collect(Collectors.toList()))
                .build();
    }
}
