package com.example.pladialmserver.global.user.entity;

import com.example.pladialmserver.office.entity.OfficeBooking;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 30)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<OfficeBooking> officeBookingList;

}
