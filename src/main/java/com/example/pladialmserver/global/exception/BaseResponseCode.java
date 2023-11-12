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
    NO_AUTHENTICATION("G0002", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // Token
    NULL_TOKEN("T0001", HttpStatus.UNAUTHORIZED, "토큰 값을 입력해주세요."),
    INVALID_TOKEN("T0002", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 값입니다."),
    UNSUPPORTED_TOKEN("T0003", HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰 값입니다."),
    MALFORMED_TOKEN("T0004", HttpStatus.UNAUTHORIZED, "잘못된 구조의 토큰 값입니다."),
    EXPIRED_TOKEN("T0005", HttpStatus.FORBIDDEN, "만료된 토큰 값입니다."),
    NOT_ACCESS_HEADER("T0006", HttpStatus.INTERNAL_SERVER_ERROR, "헤더에 접근할 수 없습니다."),
    BLACKLIST_TOKEN("T0007", HttpStatus.FORBIDDEN, "로그아웃 혹은 회원 탈퇴된 토큰입니다."),


    // User
    USER_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_EMAIL_FORMAT("U0002", HttpStatus.BAD_REQUEST, "이메일 형식을 확인해주세요."),
    INVALID_PASSWORD_FORMAT("U0003", HttpStatus.BAD_REQUEST, "비밀번호 형식을 확인해주세요."),
    NOT_EMPTY_EMAIL("U0004", HttpStatus.BAD_REQUEST, "이메일을 입력해주세요."),
    NOT_EMPTY_PASSWORD("U0005", HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),
    INVALID_PASSWORD("U0006", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_EMPTY_NAME("U0007", HttpStatus.BAD_REQUEST, "성명을 입력해주세요."),
    NOT_EMPTY_DEPARTMENT("U0008", HttpStatus.BAD_REQUEST, "부서를 입력해주세요."),
    NOT_EMPTY_PHONE("U0009", HttpStatus.BAD_REQUEST, "휴대폰 번호를 입력해주세요."),
    INVALID_PHONE_FORMAT("U0010", HttpStatus.BAD_REQUEST, "휴대폰 번호 형식을 확인해주세요."),
    NOT_EMPTY_ROLE("U0011", HttpStatus.BAD_REQUEST, "역할을 입력해주세요."),
    DEPARTMENT_NOT_FOUND("U0012", HttpStatus.NOT_FOUND, "부서를 찾을 수 없습니다."),
    ROLE_NOT_FOUND("U0013", HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다."),
    EXISTS_EMAIL("U0014", HttpStatus.CONFLICT, "존재하는 이메일입니다."),
    CAN_NOT_SEND_EMAIL("U0015", HttpStatus.INTERNAL_SERVER_ERROR, "이메일을 보낼 수 없습니다."),
    NOT_EMPTY_EMAIL_CODE("U0016", HttpStatus.BAD_REQUEST, "이메일 코드를 입력해주세요."),
    EMAIL_CODE_NOT_FOUND("U0017", HttpStatus.NOT_FOUND, "이메일 코드가 일치하지 않습니다."),
    BLACKLIST_EMAIL_CODE("U0018", HttpStatus.NOT_FOUND, "없거나 이미 만료된 이메일 코드입니다."),
    EXISTS_PHONE("U0019", HttpStatus.CONFLICT, "존재하는 휴대폰번호입니다."),
    NOT_EMPTY_AFFILIATION("U0020", HttpStatus.BAD_REQUEST, "소속을 입력해주세요."),
    AFFILIATION_NOT_FOUND("U0021", HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."),

    // Booking
    DATE_OR_TIME_IS_NULL("B0001", HttpStatus.BAD_REQUEST, "날짜와 시간을 모두 입력해주세요."),
    MEMO_SIZE_OVER("B0002", HttpStatus.BAD_REQUEST, "요청사항은 30자 이하로 작성해주세요."),
    START_TIME_MUST_BE_IN_FRONT("B0003", HttpStatus.BAD_REQUEST, "시작시간보다 끝나는 시간이 더 앞에 있습니다."),
    DATE_MUST_BE_THE_FUTURE("B0004", HttpStatus.BAD_REQUEST, "미래의 날짜를 선택해주세요."),
    ALREADY_BOOKED_TIME("B0005", HttpStatus.CONFLICT, "이미 예약되어 있는 시간입니다."),
    BOOKING_NOT_FOUND("B0006", HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    ALREADY_CANCELED_BOOKING("B0007", HttpStatus.CONFLICT, "이미 취소된 예약입니다."),
    ALREADY_FINISHED_BOOKING("B0008", HttpStatus.CONFLICT, "이미 사용이 완료된 예약입니다."),
    MUST_BE_IN_USE("B0009", HttpStatus.CONFLICT, "사용중인 상태에서만 반납이 가능합니다."),
    DATE_IS_NULL("B0010", HttpStatus.BAD_REQUEST, "날짜와 시간을 모두 입력해주세요."),
    INVALID_BOOKING_STATUS("B0011", HttpStatus.CONFLICT, "불가능한 예약 상태입니다."),

    // Office
    OFFICE_NOT_FOUND("O0001", HttpStatus.NOT_FOUND, "존재하지 않는 회의실입니다."),
    OFFICE_FACILITY_NOT_FOUND("O0002",HttpStatus.NOT_FOUND,"시설을 찾을 수 없습니다."),
    NOT_EMPTY_OFFICE_NAME("O0003", HttpStatus.BAD_REQUEST, "회의실명을 입력해주세요."),
    OFFICE_DESCRIPTION_SIZE_OVER("O0004", HttpStatus.BAD_REQUEST, "설명은 255자 이하로 작성해주세요."),
    OFFICE_NAME_SIZE_OVER("O0005", HttpStatus.BAD_REQUEST, "50자 이하로 작성해주세요."),
    NOT_EMPTY_OFFICE_LOCATION("O0006", HttpStatus.BAD_REQUEST, "회의실 위치를 입력해주세요."),
    NOT_EMPTY_OFFICE_FACILITY("O0007", HttpStatus.BAD_REQUEST, "회의실 시설을 입력해주세요."),
    NOT_EMPTY_OFFICE_CAPACITY("O0008", HttpStatus.BAD_REQUEST, "수용인원을 입력해주세요."),
    NOT_EMPTY_OFFICE_DESCRIPTION("O0009", HttpStatus.BAD_REQUEST, "회의실 설명을 입력해주세요."),
    INVALID_STATUS_BY_OFFICE_DELETION("O0010", HttpStatus.CONFLICT,"해당 회의실의 예약 현황 수정이 필요합니다."),

    // Resource
    NAME_OR_DATE_IS_NULL("R0001",HttpStatus.BAD_REQUEST,"장비 이름과 예약 날짜를 모두 입력해주세요."),
    END_DATE_BEFORE_START_DATE("R0002",HttpStatus.BAD_REQUEST,"종료일은 시작일보다 빠를 수 없습니다."),
    RESOURCE_NOT_FOUND("R0003", HttpStatus.NOT_FOUND, "존재하지 않는 장비입니다."),
    DESCRIPTION_SIZE_OVER("R0004", HttpStatus.BAD_REQUEST, "설명은 255자 이하로 작성해주세요."),
    RESOURCE_NAME_SIZE_OVER("R0005", HttpStatus.BAD_REQUEST, "장비명은 50자 이하로 작성해주세요."),
    NOT_EMPTY_LOCATION("R0006", HttpStatus.BAD_REQUEST, "보관장소를 입력해주세요."),
    NOT_EMPTY_RESOURCE_NAME("R0007", HttpStatus.BAD_REQUEST, "장비명을 입력해주세요."),
    NOT_EMPTY_RESPONSIBILITY("R0008", HttpStatus.BAD_REQUEST, "책임자를 입력해주세요."),
    NOT_EMPTY_DESCRIPTION("R0009", HttpStatus.BAD_REQUEST, "설명을 입력해주세요."),
    INVALID_STATUS_BY_RESOURCE_DELETION("R0010", HttpStatus.CONFLICT, "해당 장비의 예약 현황 수정이 필요합니다."),
    START_DATE_OR_END_DATE_IS_NULL("R0001",HttpStatus.BAD_REQUEST,"시작,종료 날짜와 시간을 모두 입력해주세요."),


    // Car
    CAR_NOT_FOUND("C0001", HttpStatus.NOT_FOUND, "존재하지 않는 차량입니다."),

    //Equipment
    INVALID_REGISTER_EQUIPMENT_REQUEST("E0001", HttpStatus.BAD_REQUEST, "부적절한 비품 등록 요청입니다. 공백및 특수문자를 제외하고 다시 입력해주세요." ),
    EQUIPMENT_NOT_FOUND("E0002", HttpStatus.NOT_FOUND, "존재하지 않는 비품입니다." ),
    INVALID_UPDATE_EQUIPMENT_REQUEST("E0003", HttpStatus.BAD_REQUEST, "부적절한 비품 수정 요청입니다. 공백및 특수문자를 제외하고 다시 입력해주세요." )
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
