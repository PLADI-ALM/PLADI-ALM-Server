package com.example.pladialmserver.global.response;



import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseCustom<T>{

    private final int status;

    private final String code;

    private final String message;

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
