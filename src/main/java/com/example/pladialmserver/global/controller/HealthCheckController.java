package com.example.pladialmserver.global.controller;

import com.example.pladialmserver.global.response.ResponseCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

  @GetMapping("/health")
  public ResponseCustom<?> healthCheck() {
    return ResponseCustom.OK("ok");
  }
}
