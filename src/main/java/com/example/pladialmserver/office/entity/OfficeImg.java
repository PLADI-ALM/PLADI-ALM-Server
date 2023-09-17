package com.example.pladialmserver.office.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficeImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeImgId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "office_id")
    private Office office;

    @NotNull
    private String imgUrl;

}
