package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duty {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    @Builder
    public Duty(String title, LocalDate startDate, LocalDate endDate, String description) {
        validateDuty(title, startDate, endDate);

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    private void validateDuty(String title, LocalDate startDate, LocalDate endDate) {
        Validator.check(title == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(startDate == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(endDate == null, ExceptionCode.NO_EMPTY_VALUE);

        if (startDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

    public Component toComponent(Long resumeId) {
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, this.title, this.startDate, this.endDate, resumeId, List.of(description));
    }

    static Duty of(DutyConverter converter) {
        return Duty.builder()
                .title(converter.title.getContent())
                .startDate(converter.title.getStartDate())
                .endDate(converter.title.getEndDate())
                .description(converter.details.description.getContent())
                .build();
    }

    @Builder
    static class DutyConverter {

        private Component title;

        private DutyDetails details;

        @Builder
        private static class DutyDetails {

            private Component description;

            public static DutyDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return DutyDetails.builder()
                        .description(componentMap.get(DESCRIPTION))
                        .build();
            }

        }

        static DutyConverter of(Component component) {
            return DutyConverter.builder()
                    .title(component)
                    .details(DutyDetails.of(component))
                    .build();
        }

    }

}
