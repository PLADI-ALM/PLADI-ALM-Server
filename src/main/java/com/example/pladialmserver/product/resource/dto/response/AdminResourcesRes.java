package com.example.pladialmserver.product.resource.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminResourcesRes {
    @Schema(type = "Long", description = "장비/차량 Id", example = "1")
    private Long id;

    @Schema(type = "String", description = "장비/차량명", example = "MacBook Pro")
    private String name;

    @Schema(type = "String", description = "제조사", example = "Apple")
    private String manufacturer;

    @Schema(type = "String", description = "현재위치", example = "3층 A홀")
    private String location;

    @Schema(type = "String", description = "책임자 이름", example = "박소정")
    private String responsibilityName;

    @Schema(type = "String", description = "책임자 연락처", example = "010-1004-1004")
    private String responsibilityPhone;

    @Schema(type = "String", description = "설명", example = "맥북프로사줘")
    private String description;

    @Schema(type = "Boolean", description = "활성화 유무", example = "'true' / 'false'")
    private Boolean isActive;

    @QueryProjection
    public AdminResourcesRes(Long id, String name, String manufacturer, String location, String responsibilityName, String responsibilityPhone, String description, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.location = location;
        this.responsibilityName = responsibilityName;
        this.responsibilityPhone = responsibilityPhone;
        this.description = description;
        this.isActive = isActive;
    }
}
