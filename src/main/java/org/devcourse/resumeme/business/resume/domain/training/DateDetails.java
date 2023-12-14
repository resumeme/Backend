package org.devcourse.resumeme.business.resume.domain.training;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;

import static org.devcourse.resumeme.common.util.Validator.notNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateDetails {

    @Getter
    private LocalDate admissionDate;

    @Getter
    private LocalDate graduationDate;

    public DateDetails(LocalDate admissionDate, LocalDate graduationDate) {
        validateDateDetails(admissionDate, graduationDate);
        this.admissionDate = admissionDate;
        this.graduationDate = graduationDate;
    }

    private static void validateDateDetails(LocalDate admissionDate, LocalDate graduationDate) {
        notNull(admissionDate);
        notNull(graduationDate);

        if (admissionDate.isAfter(graduationDate)) {
            throw new CustomException(ExceptionCode.TIME_ERROR);
        }
    }

}
