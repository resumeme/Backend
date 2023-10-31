package org.devcourse.resumeme.global.advice.exception;

public enum ExceptionCode {
    MENTEE_NOT_FOUND("해당 멘티를 찾을 수 없습니다."),
    RESUME_NOT_FOUND("해당 이력서를 찾을 수 없습니다."),
    NO_EMPTY_VALUE("빈 값일 수 없습니다"),
    MENTEE_ONLY_RESUME("멘티만 이력서를 작성할 수 있습니다."),
    ROLE_NOT_ALLOWED("허용되지 않은 역할입니다."),
    MENTOR_ALREADY_APPROVED("이미 승인된 멘토입니다.");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
