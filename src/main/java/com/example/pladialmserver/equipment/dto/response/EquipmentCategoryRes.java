package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class EquipmentCategoryRes {
    @Schema(type = "String", description = "비품 카테고리 이름", example = "기타")
    private String name;

    @Builder
    public EquipmentCategoryRes(String name) {
        this.name = name;
    }

    public static EquipmentCategoryRes toDto(EquipmentCategory category) {
        return EquipmentCategoryRes.builder()
                .name(category.getName())
                .build();
    }
}
