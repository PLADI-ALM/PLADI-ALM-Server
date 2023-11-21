package com.example.pladialmserver.product.resource.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.resource.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminProductDetailsRes {
    @Schema(type = "Long", description = "id", example = "1")
    private Long id;
    @Schema(type = "String", description = "이미지", example = "photo/ex.png")
    private String imgUrl;
    @Schema(type = "String", description = "책임자 이름", example = "이승학")
    private String responsibilityName;
    @Schema(type = "String", description = "책임자 연락처", example = "010-0000-0000")
    private String responsibilityPhone;
    @Schema(type = "String", description = "설명", example = "에스클라스")
    private String description;
    private List<ProductList> productList;

    public static AdminProductDetailsRes toDto(Resource resource, List<ProductList> productLists) {
        return AdminProductDetailsRes.builder()
                .id(resource.getResourceId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(resource.getImgKey()))
                .description(resource.getDescription())
                .responsibilityName(resource.getUser().getName())
                .responsibilityPhone(resource.getUser().getPhone())
                .productList(productLists)
                .build();
    }

    public static AdminProductDetailsRes toDto(Car car, List<ProductList> productLists) {
        return AdminProductDetailsRes.builder()
                .id(car.getCarId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(car.getImgKey()))
                .description(car.getDescription())
                .responsibilityName(car.getUser().getName())
                .responsibilityPhone(car.getUser().getPhone())
                .productList(productLists)
                .build();
    }
}
