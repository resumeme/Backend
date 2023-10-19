package org.devcourse.resumeme.global.advice.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {

    @Getter
    private String code;
    private String message;

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
