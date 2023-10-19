package org.devcourse.resumeme.global.advice.exception;

public record ErrorResponse(
        String message,
        String code
) {

}
