package com.example.pladialmserver.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {

    BOOKED("예약중"),
    FINISHED("완료"),
    CANCELED("취소");

    private final String value;

}
