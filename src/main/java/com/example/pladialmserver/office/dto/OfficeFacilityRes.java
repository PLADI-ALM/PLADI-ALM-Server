package com.example.pladialmserver.office.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class OfficeFacilityRes {
    private String officeFacility;

    public static OfficeFacilityRes toDto(String officeFacility){
        return OfficeFacilityRes.builder()
                .officeFacility(officeFacility)
                .build();
    }
}
