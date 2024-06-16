package org.devcourse.resumeme.business.event.exception;

import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

public class EventException extends CustomException {

    public EventException(String code, String message) {
        super(code, message);
    }

    public EventException(ExceptionCode exceptionCode) {
        super(exceptionCode.name(), exceptionCode.getMessage());
    }

}
