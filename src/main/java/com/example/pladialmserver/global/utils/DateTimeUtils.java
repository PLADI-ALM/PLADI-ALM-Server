package com.example.pladialmserver.global.utils;

import java.time.LocalDateTime;

public class DateTimeUtils {
  public static LocalDateTime now() {
    return LocalDateTime.now().plusHours(9);
  }
}
