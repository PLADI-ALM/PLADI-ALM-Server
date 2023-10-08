package com.example.pladialmserver.resource.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ResourceReq {
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;
}
