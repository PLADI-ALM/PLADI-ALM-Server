package com.example.pladialmserver.office.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfficeReservatorRes {
    @Schema(type = "String", description = "예약자 이름", example = "홍길동")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 전화번호", example = "010-0000-0000")
    private String reservatorPhone;
    @Schema(type = "String", description = "부서", example = "미디어")
    private String department;

    @QueryProjection
    public OfficeReservatorRes(String reservatorName, String reservatorPhone, String department) {
        this.reservatorName = reservatorName;
        this.reservatorPhone = reservatorPhone;
        this.department = department;
    }
}