package com.example.pladialmserver.user.service.model;

import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.EmailPWReq;
import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;

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
                .password("010-0000-0000")
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
}
