package org.devcourse.resumeme.global.exception.advice;

import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ErrorResponse customExceptionHandle(CustomException exception) {
        return new ErrorResponse(exception.getMessage(), exception.getCode());
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse httpRequestMethodNotSupportedExceptionHandle(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponse("endpoint, method를 다시 확인해주세요", "NOT_MATCH_METHOD_ENDPOINT");
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpRequestMethodNotSupportedExceptionHandle(HttpMessageNotReadableException exception) {
        return new ErrorResponse("Request 데이터가 올바르지 않습니다.", "NOT_MATCH_REQUEST_BODY");
    }

}
