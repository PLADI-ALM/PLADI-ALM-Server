package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EquipmentCategoryRes {
    @Schema(type = "String", description = "비품 카테고리 이름", example = "기타")
    private List<CategoryNameRes> categoryNames;

    @Builder
    public EquipmentCategoryRes(List<CategoryNameRes> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public static EquipmentCategoryRes toDto(List<EquipmentCategory> categories) {
        return EquipmentCategoryRes.builder()
                .categoryNames(categories.stream().map(CategoryNameRes::toDto).collect(Collectors.toList()))
                .build();
    }
}