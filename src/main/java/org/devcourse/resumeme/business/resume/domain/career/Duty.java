package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.DUTY;
import static org.devcourse.resumeme.business.resume.domain.Property.END_DATE;
import static org.devcourse.resumeme.business.resume.domain.Property.START_DATE;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duty {

    private Long id;

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

    public Duty(Map<String, String> component, int id) {
        this(component.get(DUTY.name() + TITLE.name() + id), LocalDate.parse(component.get(DUTY.name() + TITLE + id + START_DATE)),
                LocalDate.parse(component.get(DUTY.name() + TITLE + id + END_DATE )), component.get(DUTY.name() + DESCRIPTION.name() + id));
    }

    private void validateDuty(String title, LocalDate startDate, LocalDate endDate) {
        Validator.check(title == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(startDate == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(endDate == null, ExceptionCode.NO_EMPTY_VALUE);

        if (startDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
