package com.example.pladialmserver.user.service.model;

import com.example.pladialmserver.user.dto.request.AdminUpdateUserReq;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.EmailPWReq;
import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class TestUserInfo {

    public static final String PASSWORD = "asdf1234!";


    public static EmailPWReq setUpEmailPWReq(String email, String pw){
        return EmailPWReq.builder()
                .email(email)
                .password(pw)
                .fcmToken("1234545")
                .build();
    }

    public static CreateUserReq setUpCreateUserReq(String email, String pw){
        return new CreateUserReq(
                "홍길동",
                email,
                pw,
                setUpDepartment().getName(),
                setUpAffiliation().getName(),
                "010-0000-0000",
                Role.ADMIN.getValue(),
                "1234545");
    }

    public static User setUpUser(Long userId, Role role, Department department, Affiliation affiliation, String password){
        return User.builder()
                .id(userId)
                .name("홍길동")
                .email("test@email.com")
                .password(password)
                .phone("010-0000-0000")
                .department(department)
                .role(role)
                .assets("A12345")
                .affiliation(affiliation)
                .build();
    }

    public static Department setUpDepartment(){
        return Department.builder()
                .name("마케팅")
                .build();
    }

    public static Affiliation setUpAffiliation(){
        return Affiliation.builder()
                .name("플래디")
                .build();
    }

    public static Page<User> setUpUserResListDto(){
        List<User> userResList = new ArrayList<>();
        userResList.add(setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), "adsf1234!"));
        userResList.add(setUpUser(2L, Role.BASIC, setUpDepartment(), setUpAffiliation(), "adsf1234!"));
        userResList.add(setUpUser(3L, Role.BASIC, setUpDepartment(), setUpAffiliation(), "adsf1234!"));
        userResList.add(setUpUser(4L, Role.BASIC, setUpDepartment(), setUpAffiliation(), "adsf1234!"));
        userResList.add(setUpUser(5L, Role.BASIC, setUpDepartment(), setUpAffiliation(), "adsf1234!"));
        return new PageImpl<>(userResList);
    }

    public static AdminUpdateUserReq setUpAdminUpdateUserReq(){
        return new AdminUpdateUserReq("홍길동", "마케팅", "010-1111-1111", "플래디", "일반", "A123434, B123434");
    }
}
