package com.example.pladialmserver.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateProductReq {
    @Schema(type = "String", description = "이름", maxLength = 50)
    @Size(max = 50, message = "P0001")
    @NotBlank(message = "P0002")
    private String name;

    @Schema(type = "String", description = "제조사", maxLength = 30)
    @Size(max = 30, message = "P0003")
    private String manufacturer;

    @Schema(type = "String", description = "보관장소")
    @NotBlank(message = "P0004")
    private String location;

    @Schema(type = "Long", description = "책임자 Id")
    @NotNull(message = "P0006")
    private Long responsibility;

    @Schema(type = "String", description = "설명", maxLength = 255)
    @Size(max = 255, message = "P0007")
    @NotBlank(message = "P0008")
    private String description;

    @Schema(type = "String", description = "이미지")
    private String imgKey;
}
