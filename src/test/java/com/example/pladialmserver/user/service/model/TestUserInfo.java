package com.example.pladialmserver.user.service.model;

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

    public static User setUpUser(Department department, Affiliation affiliation, String password){
        return User.builder()
                .userId(1L)
                .name("홍길동")
                .email("test@email.com")
                .password(password)
                .fcmToken("1234545")
                .phone("010-0000-0000")
                .department(department)
                .role(Role.ADMIN)
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
