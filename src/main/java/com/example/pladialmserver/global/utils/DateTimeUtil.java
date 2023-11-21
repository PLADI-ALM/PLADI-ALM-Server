package com.example.pladialmserver.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.pladialmserver.global.Constants.*;

public class DateTimeUtil {

    // localDate + localTime => string(YYYY-MM-DD HH:MM)
    public static String dateAndTimeToString(LocalDate date, LocalTime time) {
        return localDateAndTimeToLocalDateTime(date, time).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    // localDate => string(YYYY-MM-DD)
    public static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    // localTime => string(HH:MM)
    public static String timeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    // localDateTime => string(YYYY-MM-DD HH:MM)
    public static String dateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    // localDateTime => Null / string(YYYY-MM-DD HH:MM)
    public static String dateTimeToStringNullable(LocalDateTime localDateTime) {
        return localDateTime == null ? null : dateTimeToString(localDateTime);
    }

    // string(YYYY-MM) -> localDate
    public static LocalDate stringToLocalDate(String stringDate) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
    }

    // string(YYYY-MM) -> localDate (해당 월의 첫 날)
    public static LocalDate stringToFirstLocalDate(String stringDate) {
        return stringToLocalDate(stringDate + "-01");
    }

    // localDate and Local Time -> localDateTime
    public static LocalDateTime localDateAndTimeToLocalDateTime(LocalDate localDate, LocalTime localTime){
        return LocalDateTime.of(localDate, localTime);
    }

    // localDateTime -> LocalDate
    public static LocalDate dateTimeToDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    // localDateTime -> LocalTime
    public static LocalTime dateTimeToTime(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime();
    }

    // localDateTime -> localDate + 00:00
    public static LocalDateTime getMidNightDateTime(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    // localDateTime => string(HH:MM)
    public static String dateTimeToStringTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }
}
