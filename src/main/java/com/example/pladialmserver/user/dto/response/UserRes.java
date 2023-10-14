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
    @Schema(type = "String", description = "직위", example = "팀장")
    private String position;
    @Schema(type = "String", description = "직책", example = "마케팅 팀장")
    private String officeJob;
    @Schema(type = "String", description = "역할(일반|관리자)", example = "일반")
    private String role;

    public static UserRes toDto (User user){
        return UserRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .department(user.getDepartment().getName())
                .position(user.getPosition().getName())
                .officeJob(user.getOfficeJob())
                .role(user.getRole().getValue())
                .build();
    }
}
