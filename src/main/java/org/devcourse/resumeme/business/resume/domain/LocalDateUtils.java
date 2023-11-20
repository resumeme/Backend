package org.devcourse.resumeme.business.resume.domain;

import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LocalDateUtils {

    public static LocalDate parse(String date) {
        if (date == null) {
            return null;
        }

        return LocalDate.parse(date);
    }

}
