package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminResourcesDetailsRes {
    @Schema(type="Long", description="자원 id", example ="1")
    private Long resourceId;
    @Schema(type="String", description="자원 이미지", example ="photo/ex.png")
    private String imgUrl;
    @Schema(type="String", description="설명", example ="에스클라스")
    private String description;
    private List<ResourcesList> resourcesLists;

    public static AdminResourcesDetailsRes toDto(Resource resource, List<ResourcesList> resourcesLists){
        return AdminResourcesDetailsRes.builder()
                .resourceId(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .description(resource.getDescription())
                .resourcesLists(resourcesLists)
                .build();
    }
}
