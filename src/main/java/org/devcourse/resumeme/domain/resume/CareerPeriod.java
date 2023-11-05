package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class CareerPeriod {

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private boolean isCurrentlyEmployed;

    public CareerPeriod(LocalDate careerStartDate, LocalDate endDate, boolean isCurrentlyEmployed) {
        validateCareerPeriod(careerStartDate, endDate, isCurrentlyEmployed);

        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.isCurrentlyEmployed = isCurrentlyEmployed;
    }

    private void validateCareerPeriod(LocalDate careerStartDate, LocalDate endDate, boolean isCurrentlyEmployed) {
        check(careerStartDate == null, ExceptionCode.NO_EMPTY_VALUE);
        if (!isCurrentlyEmployed) {
            check(endDate == null, ExceptionCode.NO_EMPTY_VALUE);
        }
        if (!isCurrentlyEmployed && careerStartDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
