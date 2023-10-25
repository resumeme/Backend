package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duty {

    @Getter
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    public Duty(String title, LocalDate startDate, LocalDate endDate, String description) {
        validateDuty(title, startDate, endDate);

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    private void validateDuty(String title, LocalDate startDate, LocalDate endDate) {
        validate(title == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(startDate == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(endDate == null, ExceptionCode.NO_EMPTY_VALUE);

        if (startDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
