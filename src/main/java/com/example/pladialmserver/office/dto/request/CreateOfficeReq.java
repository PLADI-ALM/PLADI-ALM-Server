package com.example.pladialmserver.office.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateOfficeReq {

    @Schema(type = "String", description = "회의실명", example = "회의실3",maxLength = 50)
    @Size(max = 50, message = "O0005")
    @NotBlank(message = "O0003")
    private String name;

    @Schema(type = "String", description = "위치", example = "S1350")
    @Size(max = 50, message = "O0005")
    @NotBlank(message = "O0006")
    private String location;

    @Schema(type = "List<String>", description = "시설(단어 사이에 큰 따움표 붙혀야함)", example = "[빔 프로젝터 , 대형 모니터 , 마이크]")
    @NotEmpty(message = "O0007")
    private List<String> facility;

    @Schema(type = "Integer", description = "수용인원", example = "6")
    @NotNull(message = "O0008")
    private String capacity;

    @Schema(type = "String", description = "회의실 설명", example = "회의실 설명",maxLength = 255)
    @Size(max = 255, message = "O0004")
    @NotBlank(message = "O0009")
    private String description;

    @Schema(type = "String", description = "회의실 이미지", example = "photo/image.key")
    private String imgUrl;

}
