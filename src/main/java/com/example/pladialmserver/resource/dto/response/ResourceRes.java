package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resource.entity.Resource;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ResourceRes {
    @Schema(type = "Long", description = "자원 Id", example = "1")
    private Long resourceId;
    @Schema(type = "String", description = "이미지 url", example = "'img.key'")
    private String imgUrl;
    @Schema(type = "String", description = "자원 이름", example = "'벤츠'")
    private String name;
    @Schema(type = "String", description = "카테고리", example = "'차량'")
    private String category;
    @Schema(type = "String", description = "설명", example = "'승학이 보물'")
    private String description;

    public static ResourceRes toDto(Resource resource){
        return ResourceRes.builder()
                .resourceId(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .name(resource.getName())
                .category(resource.getResourceCategory().getName())
                .description(resource.getDescription())
                .build();
    }

}
