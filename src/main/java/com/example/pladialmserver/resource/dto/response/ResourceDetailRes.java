package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceDetailRes {
    @Schema(type = "String", description = "장비명", example = "MacBook Pro")
    private String name;
    @Schema(type = "String", description = "보관장소", example = "S1350")
    private String location;
    @Schema(type = "Long", description = "책임자 Id", example = "1")
    private Long responsibilityId;
    @Schema(type = "String", description = "책임자 이름", example = "박소정")
    private String responsibilityName;
    @Schema(type = "String", description = "책임자 전화번호", example = "010-1234-1004")
    private String responsibilityPhone;
    @Schema(type = "String", description = "설명", example = "맥북 프로")
    private String description;
    @Schema(type = "String", description = "이미지 url", example = "https://secret-s3-bucket.s3.ap-northeast-2.amazonaws.com/img.key")
    private String imgUrl;

    public static ResourceDetailRes toDto(Resource resource) {
        return ResourceDetailRes.builder()
                .name(resource.getName())
                .location(resource.getLocation())
                .responsibilityId(resource.getUser().getUserId())
                .responsibilityName(resource.getUser().getName())
                .responsibilityPhone(resource.getUser().getPhone())
                .description(resource.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .build();
    }
}
