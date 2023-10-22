package org.devcourse.resumeme.global.advice.exception;

public enum ExceptionCode {
    MENTEE_NOT_FOUND("해당 멘티를 찾을 수 없습니다.");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
