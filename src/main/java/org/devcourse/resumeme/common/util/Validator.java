package org.devcourse.resumeme.common.util;

import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Validator {

    public static void validate(boolean predicate, String errorCode, String errorMessage) {
        if (predicate) {
            throw new CustomException(errorCode, errorMessage);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Condition {

        public static boolean isBlank(String target) {
            return !StringUtils.hasLength(target);
        }

    }
}
