package com.example.pladialmserver.global;

public class Constants {
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_HOUR_PATTERN = "yyyy-MM-dd HH";

    public static class EmailNotification {
        public static final String SPACE = " ";
        public static final String COMPANY_NAME = "[플레디] ";
        public static final String FIND_EMAIL_CODE_TITLE = "사내 시스템 이메일 인증 번호";
        public static final String NEW_BOOKING_TEXT = "새로운 예약 요청이 들어왔습니다. 예약 상태를 확인해주세요.";
        public static final String APPROVE_BOOKING_TEXT = "요청하신 예약이 승인되었습니다.";
        public static final String REJECT_BOOKING_TEXT = "요청하신 예약이 반려되었습니다.";
        public static final String RETURN_BOOKING_TEXT = "요청하신 예약 반납 확인되었습니다.";
        public static final String END_BOOKING_TEXT = "요청하신 예약 시간이 종료되었습니다.";
        public static final String BOOKING_TEXT = "예약 ";
        public static final String BOOKING_REQUEST = "요청";
        public static final String BOOKING_APPROVE = "승인";
        public static final String BOOKING_REJECT = "반려";
        public static final String BOOKING_RETURN = "반납";
        public static final String BOOKING_END = "종료";
        public static final String EMAIL_CODE = "emailCode";
        public static final String EMAIL_TEMPLATE = "email";
        public static final String BOOKING_TEMPLATE = "booking";
        public static final String RESOURCE = "장비";
        public static final String CAR = "차량";
        public static final String OFFICE = "회의실";
        public static final String PRODUCT = "자원명";

    }

    public static class Booking{
        public static final String BOOKED_TIMES = "bookedTimes";
    }

    public static class JWT{
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "bearer ";
        public static final String CLAIM_NAME = "userIdx";
        public static final String LOGOUT = "logout";
        public static final String SIGNOUT = "signout";
    }

    public static class NotificationCategory{
        public static final String CAR = "차량";
        public static final String EQUIPMENT = "장비";
        public static final String OFFICE = "회의실";
    }

    public static class NotificationType{
        public static final String SUCCESS = "예약이 완료되었습니다.";
        public static final String DENIED = "예약이 반려되었습니다.";
        public static final String CANCELED = "예약이 취소되었습니다.";
        public static final String RETURNED = "예약이 반납되었습니다.";
    }
}
