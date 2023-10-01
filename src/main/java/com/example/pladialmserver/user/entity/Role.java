package com.example.pladialmserver.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  BASIC("일반"),
  ADMIN("관리자");

  private final String value;
}
