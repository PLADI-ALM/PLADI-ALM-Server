package com.example.pladialmserver.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.pladialmserver.global.Constants.*;

public class DateTimeUtil {

    // localDate + localTime => string(YYYY-MM-DD HH:MM)
    public static String dateAndTimeToString(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
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
}
