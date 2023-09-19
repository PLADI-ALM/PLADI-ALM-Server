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

    //office
    NOT_DATE_TIME("B0001", HttpStatus.BAD_REQUEST,"날짜나 시간을 모두 입력해주세요"),

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
