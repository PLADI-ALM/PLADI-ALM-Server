package com.example.pladialmserver.car.dto.response;

import com.example.pladialmserver.car.entity.Car;
import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarDetailRes {
    @Schema(type = "String", description = "차량명", example = "벤츠")
    private String name;
    @Schema(type = "String", description = "보관장소", example = "지하 3층 주차장")
    private String location;
    @Schema(type = "Long", description = "책임자 Id", example = "1")
    private Long responsibilityId;
    @Schema(type = "String", description = "책임자 이름", example = "박소정")
    private String responsibilityName;
    @Schema(type = "String", description = "책임자 전화번호", example = "010-1234-1004")
    private String responsibilityPhone;
    @Schema(type = "String", description = "설명", example = "차 키는 ")
    private String description;
    @Schema(type = "String", description = "이미지 url", example = "https://secret-s3-bucket.s3.ap-northeast-2.amazonaws.com/img.key")
    private String imgUrl;

    public static CarDetailRes toDto(Car car) {
        return CarDetailRes.builder()
                .name(car.getName())
                .location(car.getLocation())
                .responsibilityId(car.getUser().getUserId())
                .responsibilityName(car.getUser().getName())
                .responsibilityPhone(car.getUser().getPhone())
                .description(car.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(car.getImgKey()))
                .build();
    }
}
