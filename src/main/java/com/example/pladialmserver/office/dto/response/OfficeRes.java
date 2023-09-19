package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OfficeRes {
    private String name;
    private String location;
    private Integer capacity;
    private List<String> facilityList;
    private String description;

    public static OfficeRes toDto(Office office, List<Facility> facilities){
        return OfficeRes.builder()
                .name(office.getName())
                .location(office.getLocation())
                .capacity(office.getCapacity())
                .facilityList(facilities.stream().map(Facility::getName).collect(Collectors.toList()))
                .description(office.getDescription())
                .build();
    }
}
