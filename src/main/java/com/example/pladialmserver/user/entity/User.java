package com.example.pladialmserver.user.entity;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.entityListener.UserEntityListener;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.UpdateUserReq;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET is_enable = false, update_at = current_timestamp WHERE user_id = ?")
@EntityListeners(UserEntityListener.class)
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

    @NotNull
    @Size(max = 20)
    private String phone;

    @Size(max = 300)
    private String fcmToken;

    @Size(max = 300)
    private String asserts;

    @ManyToOne
    @JoinColumn(nullable = false, name = "department_id")
    private Department department;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<OfficeBooking> officeBookingList = new ArrayList<>();

    @Builder
    public User(String name, String email, String password, Department department, String phone, Role role, String fcmToken, String asserts) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.phone=phone;
        this.role = role;
        this.fcmToken = fcmToken;
        this.asserts = asserts;
    }

    public static User toEntity(CreateUserReq req, Department department){
        return User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .phone(req.getPhone())
                .department(department)
                .role(Role.getRoleByName(req.getRole()))
                .asserts(req.getAsserts())
                .build();
    }

    public void updateUser(UpdateUserReq req, Department department){
        if(!req.getName().equals(name)) name = req.getName();
        if(!department.equals(this.department)) this.department = department;
        if(!req.getPhone().equals(phone)) phone = req.getPhone();
        if(!req.getAsserts().equals(asserts)) asserts = req.getAsserts();
        Role reqRole = Role.getRoleByName(req.getRole());
        if(!reqRole.equals(role)) role = reqRole;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public boolean checkRole(Role role) {
        return this.role == role;
    }

}
