package com.example.pladialmserver.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {

    WAITING("예약대기"),
    BOOKED("예약중"),
    USING("사용중"),
    FINISHED("사용완료"),
    CANCELED("예약취소");

    private final String value;

    // 회의실 예약 내역 상태 확인
    public static List<BookingStatus> getActiveStatuses() {
        return Arrays.asList(WAITING, BOOKED, USING);
    }

}
