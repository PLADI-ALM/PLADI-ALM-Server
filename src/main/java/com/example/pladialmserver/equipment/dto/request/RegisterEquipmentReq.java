package com.example.pladialmserver.equipment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class RegisterEquipmentReq {

    @Schema(type = "String", description = "이름", example = "맥심커피", required = true)
    @NotBlank(message = "E0001")
    private String name;

    @Schema(type = "Integer", description = "수량", example = "10", required = true)
    private int quantity;

    @Schema(type = "String", description = "카테고리", example = "기타", required = true)
    @NotBlank(message = "E0001")
    private String category;

    @Schema(type = "String", description = "위치", example = "S1305", required = true)
    @NotBlank(message = "E0001")
    private String location;

    @Schema(type = "String", description = "설명", example = "맥심커피입니다.", required = true)
    @NotBlank(message = "E0001")
    private String description;

    @Schema(type = "String", description = "이미지키", example = "photo/maxim.png", required = true)
    @NotBlank(message = "E0001")
    private String imgKey;
}