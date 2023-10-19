package org.devcourse.global.advice.exception;

public record ErrorResponse(
        String message,
        String code
) {

}
