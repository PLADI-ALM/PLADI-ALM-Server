package com.example.pladialmserver.product.car.dto;

import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarRes {
    @Schema(type = "Long", description = "차량 Id", example = "1")
    private Long carId;
    @Schema(type = "String", description = "이미지 url", example = "url")
    private String imgUrl;
    @Schema(type = "String", description = "차량 이름", example = "32라1203")
    private String name;
    @Schema(type = "String", description = "제조사", example = "벤츠")
    private String manufacturer;
    @Schema(type = "String", description = "보관장소", example = "4층 창고")
    private String location;
    @Schema(type = "String", description = "설명", example = "승학이 차량")
    private String description;

    public static CarRes toDto(Car car){
        return CarRes.builder()
                .carId(car.getCarId())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(car.getImgKey()))
                .name(car.getName())
                .location(car.getLocation())
                .description(car.getDescription())
                .build();
    }

    //TODO:Query dsl으로 리펙토링
    @QueryProjection
    public CarRes(Long carId, String imgUrl, String name, String manufacturer, String location, String description){
        this.carId=carId;
        this.imgUrl=AwsS3ImageUrlUtil.toUrl(imgUrl);
        this.name=name;
        this.manufacturer=manufacturer;
        this.location=location;
        this.description=description;
    }





}
