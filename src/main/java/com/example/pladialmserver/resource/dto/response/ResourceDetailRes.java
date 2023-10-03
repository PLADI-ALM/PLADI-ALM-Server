package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resource.entity.Resource;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceDetailRes {
    private String name;
    private String category;
    private String description;
    private String imgUrl;

    public static ResourceDetailRes toDto(Resource resource) {
        return ResourceDetailRes.builder()
                .name(resource.getName())
                .category(resource.getCategory().getValue())
                .description(resource.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgUrl()))
                .build();
    }
}
