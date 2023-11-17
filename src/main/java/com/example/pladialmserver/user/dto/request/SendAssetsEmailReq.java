package com.example.pladialmserver.user.dto.request;

import com.example.pladialmserver.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendAssetsEmailReq {
    private String name;
    private String department;
    private String affiliation;
    private String assets;

    public static SendAssetsEmailReq toDto(User user){
        return SendAssetsEmailReq.builder()
                .name(user.getName())
                .department(user.getDepartment().getName())
                .affiliation(user.getAffiliation().getName())
                .assets(user.getAssets())
                .build();
    }
}
