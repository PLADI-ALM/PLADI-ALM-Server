package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.resource.dto.request.CreateOfficeReq;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_enable = true")
public class Office extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeId;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 30)
    private String location;

    @NotNull
    private Integer capacity;

    @Size(max = 30)
    private String description;

    private String imgKey;

    @OneToMany(mappedBy = "office")
    private List<OfficeFacility> facilityList = new ArrayList<>();
    @Builder
     public Office(String name, String location, Integer capacity, String description, String imgKey){
         this.name=name;
         this.location=location;
         this.capacity=capacity;
         this.description=description;
         this.imgKey=imgKey;
     }

     public static Office toDto(CreateOfficeReq req){
        return Office.builder()
                .name(req.getName())
                .location(req.getLocation())
                .capacity(req.getCapacity())
                .description(req.getDescription())
                .imgKey(req.getImgUrl())
                .build();
     }

    public void updateOffice(CreateOfficeReq request) {
        if(!request.getName().equals(name)) name=request.getName();
        if(!request.getLocation().equals(location)) name=request.getLocation();
        if(!request.getCapacity().equals(capacity)) capacity=request.getCapacity();
        if(!request.getDescription().equals(description)) description=request.getDescription();
        if(!request.getImgUrl().equals(imgKey)) imgKey=request.getImgUrl();
    }
}
