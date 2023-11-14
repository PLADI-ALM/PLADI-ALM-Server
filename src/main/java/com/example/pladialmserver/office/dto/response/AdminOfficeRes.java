package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.office.entity.Office;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminOfficeRes {
    @Schema(type = "Long", description = "회의실 Id", example = "1")
    private Long officeId;
    @Schema(type = "String", description = "회의실 이름", example = "승학이 회의실")
    private String name;
    @Schema(type = "String", description = "회의실 위치", example = "401호")
    private String location;
    @Schema(type = "String", description = "회의실 수용인원", example = "6명")
    private String capacity;
    @Schema(type = "String", description = "회의실 설명", example = "'승학이 회의실'")
    private String description;
    @Schema(type = "Boolean", description = "활성화 유무", example = "'true' / 'false'")
    private Boolean isActive;

    public static AdminOfficeRes toDto(Office office){
        return AdminOfficeRes.builder()
                .officeId(office.getOfficeId())
                .name(office.getName())
                .location(office.getLocation())
                .capacity(office.getCapacity())
                .description(office.getDescription())
                .isActive(office.getIsActive())
                .build();
    }
}
