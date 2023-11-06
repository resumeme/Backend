package org.devcourse.resumeme.global.exception.advice;

public record ErrorResponse(
        String message,
        String code
) {

}
