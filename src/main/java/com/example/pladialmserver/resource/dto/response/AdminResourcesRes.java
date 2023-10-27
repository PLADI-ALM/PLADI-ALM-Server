package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.resource.entity.Resource;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminResourcesRes {
    @Schema(type = "Long", description = "장비 Id", example = "1")
    private Long resourceId;

    @Schema(type = "String", description = "장비명", example = "MacBook Pro")
    private String name;

    @Schema(type = "String", description = "보관장소", example = "3층 A홀")
    private String location;

    @Schema(type = "String", description = "책임자 이름", example = "박소정")
    private String responsibilityName;

    @Schema(type = "String", description = "책임자 연락처", example = "010-1004-10004")
    private String responsibilityPhone;

    @Schema(type = "String", description = "설명", example = "맥북프로사줘")
    private String description;

    @Schema(type = "Boolean", description = "활성화 유무", example = "'true' / 'false'")
    private Boolean isActive;

    public static AdminResourcesRes toDto(Resource resource) {
        return AdminResourcesRes.builder()
                .resourceId(resource.getResourceId())
                .name(resource.getName())
                .location(resource.getLocation())
                .responsibilityName(resource.getUser().getName())
                .responsibilityPhone(resource.getUser().getPhone())
                .description(resource.getDescription())
                .isActive(resource.getIsActive())
                .build();
    }

    @QueryProjection
    public AdminResourcesRes(Long resourceId, String name, String location, String responsibilityName, String responsibilityPhone, String description, Boolean isActive) {
        this.resourceId = resourceId;
        this.name = name;
        this.location = location;
        this.responsibilityName = responsibilityName;
        this.responsibilityPhone = responsibilityPhone;
        this.description = description;
        this.isActive = isActive;
    }
}
