package com.example.pladialmserver.resouce.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resouce.entity.Resource;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceRes {
    private Long resourceId;
    private String imgUrl;
    private String name;
    private String category;
    private String description;

    public static ResourceRes toDto(Resource resource){
        return ResourceRes.builder()
                .resourceId(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgUrl()))
                .name(resource.getName())
                .category(resource.getCategory().getValue())
                .description(resource.getDescription())
                .build();
    }

}
