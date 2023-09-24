package com.example.pladialmserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BaseResponseCode {

    SUCCESS("S0001", HttpStatus.OK, "요청에 성공했습니다."),

    BAD_REQUEST("G0001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // User
    USER_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // Booking
    DATE_OR_TIME_IS_NULL("B0001", HttpStatus.BAD_REQUEST, "날짜와 시간을 모두 입력해주세요."),
    MEMO_SIZE_OVER("B0002", HttpStatus.BAD_REQUEST, "요청사항은 30자 이하로 작성해주세요."),
    START_TIME_MUST_BE_IN_FRONT("B0003", HttpStatus.BAD_REQUEST, "시작시간보다 끝나는 시간이 더 앞에 있습니다."),
    DATE_MUST_BE_THE_FUTURE("B0004", HttpStatus.BAD_REQUEST, "미래의 날짜를 선택해주세요."),
    ALREADY_BOOKED_TIME("B0005", HttpStatus.BAD_REQUEST, "이미 예약되어 있는 시간입니다."),
    BOOKING_NOT_FOUND("B0006", HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    NOT_MATCHED_BOOKING_USER("B0007", HttpStatus.NOT_FOUND, "사용자가 예약한 내역이 아닙니다."),
    ALREADY_CANCELED_BOOKING("B0008", HttpStatus.NOT_FOUND, "이미 취소된 예약입니다."),
    ALREADY_FINISHED_BOOKING("B0009", HttpStatus.NOT_FOUND, "이미 사용이 완료된 예약입니다."),

    // Office
    OFFICE_NOT_FOUND("O0001", HttpStatus.NOT_FOUND, "존재하지 않는 회의실입니다."),
    ;

    public final String code;
    public final HttpStatus status;
    public final String message;

    public static BaseResponseCode findByCode(String code) {
        return Arrays.stream(BaseResponseCode.values())
                .filter(b -> b.getCode().equals(code))
                .findAny().orElseThrow(() -> new BaseException(BAD_REQUEST));
    }

}
