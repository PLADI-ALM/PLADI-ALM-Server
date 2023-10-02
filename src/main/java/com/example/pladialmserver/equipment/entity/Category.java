package com.example.pladialmserver.equipment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

  VEHICLE("차량"),
  ACCESSORIES("악세사리"),
  PC("컴퓨터"),
  FILMING_EQUIPMENT("촬영장비"),
  ETC("기타");

  private final String value;
}
