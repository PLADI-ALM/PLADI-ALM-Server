package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.querydsl.core.annotations.QueryProjection;


import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OfficeRes {
    @Schema(type = "Long", description = "회의실 Id", example = "1")
    private Long officeId;
    @Schema(type = "String", description = "회의실 이름", example = "승학이 회의실")
    private String name;
    @Schema(type = "String", description = "회의실 위치", example = "401호")
    private String location;
    @Schema(type = "String", description = "회의실 수용인원", example = "6명")
    private Integer capacity;
    @Schema(type = "String", description = "회의실 시설물", example = "'빔 프로젝터' / '마이크' / '화상회의' / '대형 모니터'")
    private List<String> facilityList;
    @Schema(type = "String", description = "회의실 설명", example = "'승학이 회의실'")
    private String description;
    @Schema(type = "String", description = "회의실 이미지", example = "/photo/ex.png")
    private String imgUrl;
    @Schema(type = "Boolean", description = "활성화 유무", example = "'true' / 'false'")
    private Boolean isActive;

    public static OfficeRes toDto(Office office, List<Facility> facilities){
        return OfficeRes.builder()
                .officeId(office.getOfficeId())
                .name(office.getName())
                .location(office.getLocation())
                .capacity(office.getCapacity())
                .facilityList(facilities.stream().map(Facility::getName).collect(Collectors.toList()))
                .description(office.getDescription())
                .imgUrl(AwsS3ImageUrlUtil.toUrl(office.getImgKey()))
                .isActive(office.getIsActive())
                .build();
    }
//TODO query dsl적용
//    @QueryProjection
//    public OfficeRes(Long officeId, String name, String location, Integer capacity, List<String> facilityList,String description, String imgUrl){
//        this.officeId=officeId;
//        this.name=name;
//        this.location=location;
//        this.capacity=capacity;
//        this.facilityList=facilityList;
//        this.description=description;
//        this.imgUrl=imgUrl;
//    }

}
