package com.example.pladialmserver.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
  INTERN("인턴"),
  EMPLOYEE("사원"),
  MANAGER("과장"),
  ASSISTANT("대리"),
  CONDUCTOR("차장"),
  DIRECTOR("부장"),
  CEO("사장"),
  VICE_PRESIDENT("부사장"),
  CHAIRMAN("회장"),
  VICE_CHAIRMAN("부회장");

  private final String value;
}
