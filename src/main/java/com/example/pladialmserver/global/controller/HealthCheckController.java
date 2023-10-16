package com.example.pladialmserver.global.controller;

import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.global.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

  @GetMapping("/health")
  public ResponseCustom<?> healthCheck() {
    return ResponseCustom.OK("ok");
  }

  @GetMapping("time-zone")
  public ResponseCustom<?> getLocalDateTime() {
    return ResponseCustom.OK(DateTimeUtils.now().toString());
  }
}
