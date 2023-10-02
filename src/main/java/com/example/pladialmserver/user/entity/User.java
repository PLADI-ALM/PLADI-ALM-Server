package com.example.pladialmserver.user.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import lombok.AccessLevel;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 30)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @ManyToOne
    @JoinColumn(nullable = false, name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(nullable = false, name = "position_id")
    private Position position;

    @NotNull
    @Size(max = 30)
    private String officeJob;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<OfficeBooking> officeBookingList = new ArrayList<>();

}
