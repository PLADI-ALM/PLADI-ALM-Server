package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.product.resource.dto.request.CreateOfficeReq;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE office SET is_enable = false, update_at = current_timestamp WHERE office_id = ?")
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
    private String capacity;

    @Size(max = 30)
    private String description;

    private String imgKey;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "office")
    private List<OfficeFacility> facilityList = new ArrayList<>();
    @Builder
     public Office(String name, String location, String capacity, String description, String imgKey,Boolean isActive){
         this.name=name;
         this.location=location;
         this.capacity=capacity;
         this.description=description;
         this.imgKey=imgKey;
         this.isActive=isActive;
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

    public void activateOffice() {
        isActive = !isActive;
    }
}
