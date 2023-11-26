package com.example.pladialmserver.office.service.model;

import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.entity.Office;

public class TestOfficeInfo {

    public static OfficeReq setUpOfficeReq(){
        return OfficeReq.builder()
                .date(DateTimeUtil.stringToLocalDate("2023-12-01"))
                .startTime(DateTimeUtil.stringToLocalTime("11:00"))
                .endTime(DateTimeUtil.stringToLocalTime("12:00"))
                .memo("프롬프터 사용 필요")
                .build();
    }

    public static Office setUpOffice(){
        return Office.builder()
                .id(1L)
                .name("회의실 1")
                .location("2F")
                .capacity("2")
                .description("플래디 온리")
                .imgKey("asdf.png")
                .isActive(true)
                .build();
    }
}
