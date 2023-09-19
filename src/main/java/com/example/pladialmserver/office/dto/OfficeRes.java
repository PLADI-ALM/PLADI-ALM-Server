package com.example.pladialmserver.office.dto;

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
    private List<OfficeFacilityRes> facilityList;
    private String description;

    public static OfficeRes toDto(Office office, List<Facility> facilities){
        return OfficeRes.builder()
                .name(office.getName())
                .location(office.getLocation())
                .capacity(office.getCapacity())
                .facilityList(facilities.stream().map(m -> OfficeFacilityRes.toDto(m.getName())).collect(Collectors.toList()))
                .description(office.getDescription())
                .build();
//        OfficeRes officeRes=new OfficeRes();
//        officeRes.name=office.getName();
//        officeRes.location=office.getLocation();
//        officeRes.capacity=office.getCapacity();
//        officeRes.facilityList=facilities.stream().map(m -> OfficeFacilityRes.toDto(m.getName())).collect(Collectors.toList());
//        return officeRes;
    }
}
