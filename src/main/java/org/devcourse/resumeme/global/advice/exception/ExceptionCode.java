package org.devcourse.resumeme.global.advice.exception;

public enum ExceptionCode {
    MENTEE_NOT_FOUND("해당 멘티를 찾을 수 없습니다."),
    NO_EMPTY_VALUE("빈 값일 수 없습니다"),
    MENTEE_ONLY_RESUME("멘티만 이력서를 작성할 수 있습니다."),
    EVENT_NOT_FOUND("해당 이벤트를 찾을 수 없습니다."),
    CAN_NOT_CHANGE_MAX_COUNT("이미 신청한 인원수보다 적은 수로는 변경이 불가능합니다");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
