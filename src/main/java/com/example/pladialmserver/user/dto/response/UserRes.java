package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRes {
    @Schema(type = "Long", description = "1", example = "1")
    private Long userId;
    @Schema(type = "String", description = "성명", example = "홍길동")
    private String name;
    @Schema(type = "String", description = "이메일", example = "1234@email.com")
    private String email;
    @Schema(type = "String", description = "부서", example = "마케팅")
    private String department;
    @Schema(type = "String", description = "휴대폰번호", example = "010-0000-0000")
    private String phone;
    @Schema(type = "String", description = "역할(일반|관리자)", example = "일반", allowableValues = {"일반", "관리자"})
    private String role;
    @Schema(type = "String", description = "자산(컴퓨터, 태블릿)", example = "A123434, B123434")
    private String assets;
    @Schema(type = "String", description = "소속", example = "플래디", allowableValues = {"플래디", "스튜디오아이", "피디룸"})
    private String affiliation;

    public static UserRes toDto (User user){
        return UserRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .department(user.getDepartment().getName())
                .phone(user.getPhone())
                .role(user.getRole().getValue())
                .assets(user.getAssets())
                .affiliation(user.getAffiliation().getName())
                .build();
    }
}
