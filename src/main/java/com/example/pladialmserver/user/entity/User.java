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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_enable = false, update_at = current_timestamp WHERE user_id = ?")
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
    private String assets;

    @ManyToOne
    @JoinColumn(nullable = false, name = "department_id")
    private Department department;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(nullable = false, name = "affiliation_id")
    Affiliation affiliation;

    @OneToMany(mappedBy = "user")
    private List<OfficeBooking> officeBookingList = new ArrayList<>();

    @Builder
    public User(Long userId, String name, String email, String password, Department department, String phone, Role role, String fcmToken, String assets, Affiliation affiliation) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.phone=phone;
        this.role = role;
        this.fcmToken = fcmToken;
        this.assets = assets;
        this.affiliation = affiliation;
    }

    public User(Long userId, String name, String email, String password, String phone, String assets, Department department, Role role, Affiliation affiliation) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.assets = assets;
        this.department = department;
        this.role = role;
        this.affiliation = affiliation;
    }

    public static User toEntity(CreateUserReq req, Department department, Affiliation affiliation){
        return User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .phone(req.getPhone())
                .department(department)
                .role(Role.getRoleByName(req.getRole()))
                .assets(req.getAssets())
                .affiliation(affiliation)
                .build();
    }

    public void updateUser(UpdateUserReq req){
        if(!req.getName().equals(name)) name = req.getName();
        if(!req.getPhone().equals(phone)) phone = req.getPhone();
        if(!req.getAssets().equals(assets)) this.assets = req.getAssets();
    }

    public void updateRole(String role){
        Role reqRole = Role.getRoleByName(role);
        if(!reqRole.equals(this.role)) this.role = reqRole;
    }

    public void updateAffiliation(Affiliation affiliation){
        if(!affiliation.equals(this.affiliation)) this.affiliation = affiliation;
    }

    public void updateDepartment(Department department){
        if(!department.equals(this.department)) this.department = department;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public boolean checkRole(Role role) {
        return this.role == role;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
