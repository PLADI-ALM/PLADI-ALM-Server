package com.example.pladialmserver.office.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.dto.OfficeRes;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.office.service.OfficeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    /**
     * 전체 회의실 목록 조회 and 예약 가능한 회의실 목록 조회
     */
    @GetMapping()
    public ResponseCustom<List<OfficeRes>> searchOffice(
            @RequestParam(required = false ) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime
            )
    {
        // 날짜와 시작 시간 또는 종료 시간 중 하나라도 입력되지 않았다면 에러 반환
        if ((date != null && (startTime == null || endTime == null)) ||
                (date == null && (startTime != null || endTime != null))){
           throw new BaseException(BaseResponseCode.NOT_DATE_TIME);
        }
        return ResponseCustom.OK(officeService.findAvailableOffices(date, startTime, endTime));
    }
    /**
     * 회의실 개별 조회
     */



    /**
     * 회의실 일자별 예약 현황 조회
     */

    /**
     * 회의실 예약
     */

    /**
     * 회의실 예약 목록 조회
     */

    /**
     * 회의실 예약 개별 조회
     */

    /**
     * 회의실 예약 수정
     */

    /**
     * 회의실 예약 취소
     */


}
