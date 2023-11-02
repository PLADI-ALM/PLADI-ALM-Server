package com.example.pladialmserver.global;

public class Constants {
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_HOUR_PATTERN = "yyyy-MM-dd HH";

    public static class Email{
        public static final String COMPANY_NAME = "[플레디] ";
        public static final String FIND_EMAIL_CODE_TITLE = "사내 시스템 이메일 인증 번호";
        public static final String EMAIL_CODE = "emailCode";
        public static final String EMAIL = "email";
        public static final String BOOKING = "booking";
        public static final String OFFICE = "회의실";
        public static final String RESOURCE = "장비";
        public static final String CAR = "차량";
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
}
