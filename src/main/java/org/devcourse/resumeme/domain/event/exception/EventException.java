package org.devcourse.resumeme.domain.event.exception;

import org.devcourse.resumeme.global.advice.exception.CustomException;

public class EventException extends CustomException {

    public EventException(String code, String message) {
        super(code, message);
    }

}
