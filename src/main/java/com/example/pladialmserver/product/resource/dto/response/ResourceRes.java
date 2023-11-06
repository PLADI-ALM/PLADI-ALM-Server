package com.example.pladialmserver.product.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ResourceRes {
    @Schema(type = "Long", description = "장비 Id", example = "1")
    private Long resourceId;
    @Schema(type = "String", description = "이미지 url", example = "'img.key'")
    private String imgUrl;
    @Schema(type = "String", description = "장비 이름", example = "'벤츠'")
    private String name;
    @Schema(type = "String", description = "보관장소", example = "'4층 창고'")
    private String location;
    @Schema(type = "String", description = "설명", example = "'승학이 보물'")
    private String description;

    public static ResourceRes toDto(Resource resource){
        return ResourceRes.builder()
                .resourceId(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .name(resource.getName())
                .location(resource.getLocation())
                .description(resource.getDescription())
                .build();
    }

    @QueryProjection
    public ResourceRes(Long resourceId, String imgUrl, String name, String location, String description){
        this.resourceId=resourceId;
        this.imgUrl=AwsS3ImageUrlUtil.toUrl(imgUrl);
        this.name=name;
        this.location=location;
        this.description=description;
    }

}
