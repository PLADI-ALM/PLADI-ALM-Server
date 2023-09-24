package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import lombok.AccessLevel;
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

}
