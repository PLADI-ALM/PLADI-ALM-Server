package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EquipmentCategoryRes {
    @Schema(type = "List<CategoryNameRes>", description = "카테고리 목록 리스트", example = "기타")
    private List<String> categoryNames;

    @Builder
    public EquipmentCategoryRes(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public static EquipmentCategoryRes toDto(List<EquipmentCategory> categories) {
        return EquipmentCategoryRes.builder()
                .categoryNames(categories.stream().map(EquipmentCategory::getName).collect(Collectors.toList()))
                .build();
    }
}
