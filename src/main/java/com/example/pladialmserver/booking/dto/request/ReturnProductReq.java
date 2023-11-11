package com.example.pladialmserver.booking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReturnProductReq {
    @Schema(type = "String", description = "반납장소", example = "3F")
    private String returnLocation;

    @Schema(type = "String", description = "기타사항", example = "오른쪽 헤드라이트 고장")
    private String remark;
}
