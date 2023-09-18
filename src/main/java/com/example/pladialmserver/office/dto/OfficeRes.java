package com.example.pladialmserver.office.dto;

import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OfficeRes {
    private String name;
    private String location;
    private Integer capacity;
    private List<OfficeFacilityRes> facilityList;
    private String description;

    public static OfficeRes toDto(Office office, List<Facility> facilities){
        OfficeRes officeRes=new OfficeRes();
        officeRes.name=office.getName();
        officeRes.location=office.getLocation();
        officeRes.capacity=office.getCapacity();
        officeRes.facilityList=facilities.stream().map(m -> OfficeFacilityRes.toDto(m.getName())).collect(Collectors.toList());
        return officeRes;
    }
}
