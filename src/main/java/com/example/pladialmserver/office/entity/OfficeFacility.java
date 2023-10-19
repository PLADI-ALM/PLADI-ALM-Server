package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_enable = true")
public class OfficeFacility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeFacilityId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "office_id")
    private Office office;

    @ManyToOne
    @JoinColumn(nullable = false, name = "facility_id")
    private Facility facility;

    @Builder
    public OfficeFacility(Office office,Facility facility){
        this.office=office;
        this.facility=facility;
    }

    public static OfficeFacility toDto(Office office, Facility facility){
        return OfficeFacility.builder()
                .office(office)
                .facility(facility)
                .build();
    }

}
