package com.example.pladialmserver.office.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeId;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 30)
    private String location;

    @NotNull
    private Integer capacity;

    @Size(max = 30)
    private String description;

    @OneToMany(mappedBy = "office")
    private List<OfficeFacility> facilityList;

    @OneToMany(mappedBy = "office")
    private List<OfficeImg> imgList;

}
