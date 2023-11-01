package com.example.pladialmserver.product.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.product.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminResourcesDetailsRes {
    @Schema(type="Long", description="장비 id", example ="1")
    private Long resourceId;
    @Schema(type="String", description="장비 이미지", example ="photo/ex.png")
    private String imgUrl;
    @Schema(type = "String", description = "책임자 이름", example = "이승학")
    private String responsibilityName;
    @Schema(type = "String", description = "책임자 연락처", example = "010-0000-0000")
    private String responsibilityPhone;
    @Schema(type="String", description="설명", example ="에스클라스")
    private String description;
    private List<ResourcesList> resourcesLists;

    public static AdminResourcesDetailsRes toDto(Resource resource, List<ResourcesList> resourcesLists){
        return AdminResourcesDetailsRes.builder()
                .resourceId(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .description(resource.getDescription())
                .responsibilityName(resource.getUser().getName())
                .responsibilityPhone(resource.getUser().getPhone())
                .resourcesLists(resourcesLists)
                .build();
    }
}
