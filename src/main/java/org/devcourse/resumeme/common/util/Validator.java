package org.devcourse.resumeme.common.util;

import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;

@NoArgsConstructor(access = PRIVATE)
public class Validator {

    public static void check(boolean predicate, String errorCode, String errorMessage) {
        if (predicate) {
            throw new CustomException(errorCode, errorMessage);
        }
    }

    public static void check(boolean predicate, ExceptionCode exceptionCode) {
        if (predicate) {
            throw new CustomException(exceptionCode);
        }
    }

    public static void notNull(Object target) {
        if (target == null) {
            throw new CustomException(NO_EMPTY_VALUE);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Condition {

        public static boolean isBlank(String target) {
            return !StringUtils.hasLength(target);
        }

    }
}
