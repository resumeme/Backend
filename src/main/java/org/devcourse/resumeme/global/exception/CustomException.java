package org.devcourse.resumeme.global.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {

    @Getter
    private String code;
    private String message;

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.name();
        this.message = exceptionCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
