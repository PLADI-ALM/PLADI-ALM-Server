package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.resource.entity.ResourceCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AdminResourceCategoryRes {
    @Schema(type = "[String]", description = "자원 카테고리 이름", example = "[전자기기, 문구류, 차량, 기타]")
    private List<String> category;


    public static AdminResourceCategoryRes toDto(List<ResourceCategory> resourceCategories) {
        return AdminResourceCategoryRes.builder()
                .category(resourceCategories.stream().map(ResourceCategory::getName).collect(Collectors.toList()))
                .build();
    }
}