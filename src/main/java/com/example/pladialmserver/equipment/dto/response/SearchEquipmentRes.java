package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.Equipment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchEquipmentRes {
    @Schema(type = "String", description = "비품 이름", example = "비품1")
    private String name;
    @Schema(type = "int", description = "비품 수량", example = "1")
    private int quantity;
    @Schema(type = "String", description = "비품 카테고리", example = "기타")
    private String category;
    @Schema(type = "String", description = "담당자", example = "누구게")
    private String keeper;
    @Schema(type = "String", description = "연락처", example = "010-0000-0000")
    private String contact;
    @Schema(type = "String", description = "비품 위치", example = "s1350")
    private String location;
    @Schema(type = "String", description = "비품 설명", example = "블라블라")
    private String description;

    public static SearchEquipmentRes toDto(Equipment equipment) {
        return SearchEquipmentRes.builder()
                .name(equipment.getName())
                .quantity(equipment.getQuantity())
                .category(equipment.getEquipmentCategory().getName())
                .keeper(equipment.getUser().getName())
                .contact(equipment.getUser().getPhone())
                .location(equipment.getLocation())
                .description(equipment.getDescription())
                .build();
    }
}
