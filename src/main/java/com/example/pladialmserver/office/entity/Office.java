package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.office.dto.request.CreateOfficeReq;
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
     public Office(Long id, String name, String location, String capacity, String description, String imgKey,Boolean isActive){
        this.officeId = id;
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
        if (!name.equals(request.getName())) name = request.getName();
        if (!location.equals(request.getLocation())) name = request.getLocation();
        if (!capacity.equals(request.getCapacity())) capacity = request.getCapacity();
        if (!description.equals(request.getDescription())) description = request.getDescription();
        if (!imgKey.equals(request.getImgUrl())) imgKey = request.getImgUrl();
    }

    public void activateOffice() {
        isActive = !isActive;
    }
}
