package org.devcourse.resumeme.global.exception;

public class UnAuthenticatedException extends CustomException {

    public UnAuthenticatedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
