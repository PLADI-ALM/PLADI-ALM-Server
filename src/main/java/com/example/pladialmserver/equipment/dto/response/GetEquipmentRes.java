package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetEquipmentRes {
    @Schema(type = "String", description = "이름", example = "비품1")
    private String name;
    @Schema(type = "String", description = "수량", example = "10개")
    private String quantity;
    @Schema(type = "String", description = "위치", example = "S1350")
    private String location;
    @Schema(type = "String", description = "카테고리", example = "기타")
    private String category;
    @Schema(type = "String", description = "비품 설명", example = "비품1 입니다.")
    private String description;
    @Schema(type = "String", description = "비품 이미지 Url", example = "photo/maxim.png")
    private String imgUrl;

    public static GetEquipmentRes toDto(Equipment equipment) {
        return GetEquipmentRes.builder()
                .name(equipment.getName())
                .quantity(equipment.getQuantity())
                .location(equipment.getLocation())
                .category(equipment.getEquipmentCategory().getName())
                .description(equipment.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(equipment.getImgKey()))
                .build();
    }
}
