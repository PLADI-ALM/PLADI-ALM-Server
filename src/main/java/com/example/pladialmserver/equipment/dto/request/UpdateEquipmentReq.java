package com.example.pladialmserver.equipment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UpdateEquipmentReq {
    @Schema(type = "String", description = "이름", example = "맥심커피", required = true)
    @NotBlank(message = "E0003")
    private String name;
    @Schema(type = "String", description = "비품 수량", example = "10박스", required = true)
    @NotNull(message = "E0003")
    private String quantity;
    @Schema(type = "Long", description = "비품 담당자", example = "1", required = true)
    private Long register;
    @Schema(type = "String", description = "비품 위치", example = "s1350", required = true)
    @NotBlank(message = "E0003")
    private String location;
    @Schema(type = "String", description = "비품 설명", example = "블라블라", required = true)
    @NotBlank(message = "E0003")
    private String description;
    @Schema(type = "String", description = "비품 카테고리", example = "기타", required = true)
    @NotBlank(message = "E0003")
    private String category;
    @Schema(type = "String", description = "비품 이미지 키", example = "photo/maxim.png", required = true)
    @NotBlank(message = "E0003")
    private String imgKey;
}
