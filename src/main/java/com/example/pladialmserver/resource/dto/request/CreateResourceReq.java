package com.example.pladialmserver.resource.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateResourceReq {
    @Schema(type = "String", description = "장비명", maxLength = 50)
    @Size(max = 50, message = "R0005")
    @NotBlank(message = "R0007")
    private String name;

    @Schema(type = "String", description = "장비 보관장소")
    @NotBlank(message = "R0008")
    private String location;

    @Schema(type = "Long", description = "장비 책임자 Id")
    @NotBlank(message = "R0008")
    private Long responsibility;

    @Schema(type = "String", description = "장비 설명", maxLength = 255)
    @Size(max = 255, message = "R0004")
    @NotBlank(message = "R0009")
    private String description;

    @Schema(type = "String", description = "장비 이미지")
    private String imgKey;
}
