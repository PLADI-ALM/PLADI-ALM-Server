package com.example.pladialmserver.global.response;

import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseCustom<T>{
    @Schema(type = "int", description = "HTTP코드", example = "200")
    private final int status;

    @Schema(type = "String", description = "상태코드", example = "S0001")
    private final String code;

    @Schema(type = "String", description = "메세지", example = "메세지")
    private final String message;

    @Schema(description = "데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static ResponseCustom OK() {
        BaseResponseCode baseResponseCode = BaseResponseCode.SUCCESS;
        return new ResponseCustom<>(baseResponseCode.getStatus().value(), baseResponseCode.getCode(), baseResponseCode.getMessage());
    }

    public static <T> ResponseCustom<T> OK(@Nullable T data) {
        BaseResponseCode baseResponseCode = BaseResponseCode.SUCCESS;
        return new ResponseCustom<T>(baseResponseCode.getStatus().value(), baseResponseCode.getCode(), baseResponseCode.getMessage(), data);
    }

    public static ResponseCustom error(int status, String code, String message) {
        return new ResponseCustom<>(status, code, message);
    }

}
