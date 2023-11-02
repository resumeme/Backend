package org.devcourse.resumeme.global.advice;

import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

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

}
