package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OfficeRes {
    private Long officeId;
    private String name;
    private String location;
    private Integer capacity;
    private List<String> facilityList;
    private String description;
    private List<String> imgUrls;


    public static OfficeRes toDto(Office office, List<Facility> facilities,List<String> imgUrls){
        return OfficeRes.builder()
                .officeId(office.getOfficeId())
                .name(office.getName())
                .location(office.getLocation())
                .capacity(office.getCapacity())
                .facilityList(facilities.stream().map(Facility::getName).collect(Collectors.toList()))
                .description(office.getDescription())
                .imgUrls(imgUrls)
                .build();
    }
}
