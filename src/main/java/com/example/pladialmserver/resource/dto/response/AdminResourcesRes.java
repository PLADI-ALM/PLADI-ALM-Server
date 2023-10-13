package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminResourcesRes {
    @Schema(type = "Long", description = "자원 Id", example = "1")
    private Long resourceId;

    @Schema(type = "String", description = "자원명", example = "MacBook Pro")
    private String name;

    @Schema(type = "String", description = "자원 카테고리", example = "전자기기")
    private String category;

    @Schema(type = "String", description = "자원 설명", example = "맥북프로사줘")
    private String description;

    public static AdminResourcesRes toDto(Resource resource) {
        return AdminResourcesRes.builder()
                .resourceId(resource.getResourceId())
                .name(resource.getName())
                .category(resource.getResourceCategory().getName())
                .description(resource.getDescription())
                .build();
    }
}
