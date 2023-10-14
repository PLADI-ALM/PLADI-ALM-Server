package com.example.pladialmserver.resource.dto.response;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResourceRes {
    @Schema(type = "Long", description = "자원 Id", example = "1")
    private Long id;
    @Schema(type = "String", description = "자원 이름", example = "'벤츠'")
    private String name;
    @Schema(type = "String", description = "카테고리", example = "'차량'")
    private String category;
    @Schema(type = "String", description = "예약일자(시작일)", example = "'2023-10-01 12:00' / '2023-10-01'")
    private String startDateTime;
    @Schema(type = "String", description = "예약일자(종료일)", example = "'2023-10-01 13:00' / '2023-10-03'")
    private String endDateTime;
    @Schema(type = "String", description = "요청자", example = "이승학")
    private String requester;
    @Schema(type = "String", description = "직위", example = "대리")
    private String position;
    @Schema(type = "String", description = "상태", example = "'예약중' / '사용중'")
    private String status;

    public static AdminResourceRes toDto(ResourceBooking resourceBooking){
        return AdminResourceRes.builder()
                .id(resourceBooking.getResourceBookingId())
                .name(resourceBooking.getResource().getName())
                .category(resourceBooking.getResource().getResourceCategory().getName())
                .startDateTime(DateTimeUtil.dateToString(resourceBooking.getStartDate()))
                .endDateTime(DateTimeUtil.dateToString(resourceBooking.getEndDate()))
                .requester(resourceBooking.getUser().getName())
                .position(resourceBooking.getUser().getPosition().getName())
                .status(resourceBooking.getStatus().getValue())
                .build();

    }



}
