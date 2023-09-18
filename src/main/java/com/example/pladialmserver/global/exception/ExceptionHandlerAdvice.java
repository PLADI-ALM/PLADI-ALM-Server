package com.example.pladialmserver.global.exception;

import com.example.pladialmserver.global.response.ResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ResponseCustom> handleBaseException(BaseException e) {
        BaseResponseCode baseResponseCode = e.getBaseResponseCode();
        return ResponseEntity.status(baseResponseCode.getStatus())
                .body(ResponseCustom.error(baseResponseCode.getStatus().value(), baseResponseCode.getCode(), baseResponseCode.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseCustom handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = Objects.requireNonNull(e.getFieldError());
        BaseResponseCode baseResponseCode = BaseResponseCode.findByCode(fieldError.getDefaultMessage());
        return ResponseCustom.error(baseResponseCode.getStatus().value(), baseResponseCode.getCode(), baseResponseCode.getMessage());
    }

}