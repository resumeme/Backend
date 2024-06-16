package org.devcourse.resumeme.business.resume.domain.career;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CareerPeriod {

    private LocalDate startDate;

    private LocalDate endDate;

    public CareerPeriod(LocalDate startDate, LocalDate endDate) {
        validateCareerPeriod(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateCareerPeriod(LocalDate startDate, LocalDate endDate) {
        check(startDate == null, ExceptionCode.NO_EMPTY_VALUE);
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
