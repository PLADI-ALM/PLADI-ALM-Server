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
@Where(clause = "is_enable = true")
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

    @Builder
    public User(String name, String email, String password, Department department, Position position, String officeJob, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.position = position;
        this.officeJob = officeJob;
        this.role = role;
    }

    public static User toEntity(CreateUserReq req, Department department, Position position){
        return User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .department(department)
                .position(position)
                .officeJob(req.getOfficeJob())
                .role(Role.getRoleByName(req.getRole()))
                .build();
    }

    public void updateUser(UpdateUserReq req, Department department, Position position){
        if(!req.getName().equals(name)) name = req.getName();
        if(!department.equals(this.department)) this.department = department;
        if(!position.equals(this.position)) this.position = position;
        if(!req.getOfficeJob().equals(officeJob)) officeJob = req.getOfficeJob();
        Role reqRole = Role.getRoleByName(req.getRole());
        if(!reqRole.equals(role)) role = reqRole;
    }
  
    public boolean checkRole(Role role) {
        return this.role == role;
    }
  
}
