package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;

import static org.devcourse.resumeme.common.util.Validator.notNull;

@Embeddable
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
