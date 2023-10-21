package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceDetailRes {
    @Schema(type = "String", description = "자원명", example = "MacBook Pro")
    private String name;
    @Schema(type = "String", description = "카테고리", example = "PC")
    private String category;
    @Schema(type = "String", description = "설명", example = "맥북 프로 ")
    private String description;
    @Schema(type = "String", description = "이미지 url", example = "https://secret-s3-bucket.s3.ap-northeast-2.amazonaws.com/img.key")
    private String imgUrl;

    // TODO 기획 변경으로 인한 수정
    public static ResourceDetailRes toDto(Resource resource) {
        return ResourceDetailRes.builder()
                .name(resource.getName())
//                .category(resource.getResourceCategory().getName())
                .description(resource.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .build();
    }
}
