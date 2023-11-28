package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.domain.career.Duty.DutyConverter;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.COMPANY;
import static org.devcourse.resumeme.business.resume.domain.Property.CONTENT;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.DUTY;
import static org.devcourse.resumeme.business.resume.domain.Property.POSITION;
import static org.devcourse.resumeme.business.resume.domain.Property.SKILL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    private String companyName;

    private String position;

    private List<String> skills;

    private String careerContent;

    private List<Duty> duties;

    private CareerPeriod careerPeriod;

    private ComponentInfo componentInfo;

    public Career(String companyName, String position, List<String> skills, List<Duty> duties,
            LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        validateCareer(companyName, position);

        this.companyName = companyName;
        this.position = position;
        this.skills = skills;
        this.duties = duties;
        this.careerPeriod = new CareerPeriod(careerStartDate, endDate);
        this.careerContent = careerContent;
    }

    @Builder
    private Career(String companyName, String position, List<String> skills, List<Duty> duties,
            LocalDate careerStartDate, LocalDate endDate, String careerContent, Component component) {
        validateCareer(companyName, position);

        this.companyName = companyName;
        this.position = position;
        this.skills = skills;
        this.duties = duties;
        this.careerPeriod = new CareerPeriod(careerStartDate, endDate);
        this.careerContent = careerContent;
        this.componentInfo = new ComponentInfo(component);
    }

    private void validateCareer(String companyName, String position) {
        Validator.check(companyName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(position == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public Component toComponent(Long resumeId) {
        Component position = new Component(POSITION, this.position, resumeId);
        Component skills = new Component(SKILL, String.join(",", this.skills), resumeId);
        Component careerContent = new Component(CONTENT, this.careerContent, resumeId);

        Component descriptions = new Component(DESCRIPTION, null, null, null, resumeId, List.of(position, skills, careerContent));
        List<Component> duties = getDuties(resumeId);
        Component duty = new Component(DUTY, String.valueOf(duties.size()), null, null, resumeId, duties);

        return new Component(COMPANY, this.companyName, this.careerPeriod.getStartDate(), this.careerPeriod.getEndDate(), resumeId, List.of(descriptions, duty));
    }

    private List<Component> getDuties(Long resumeId) {
        return duties.stream()
                .map(duty -> duty.toComponent(resumeId))
                .toList();
    }

    public static Career of(List<Component> components) {
        CareerConverter converter = CareerConverter.of(components);

        return Career.of(converter);
    }

    public static List<Career> of(Component component) {
        return component.getComponents().stream()
                .map(CareerConverter::of)
                .map(Career::of)
                .toList();
    }

    private static Career of(CareerConverter converter) {
        return Career.builder()
                .component(converter.career)
                .companyName(converter.career.getContent())
                .careerStartDate(converter.career.getStartDate())
                .endDate(converter.career.getEndDate())
                .position(converter.details.position.getContent())
                .skills(Arrays.asList(converter.details.skills.getContent().split(",")))
                .careerContent(converter.details.careerContent.getContent())
                .duties(getDuties(converter.duty))
                .build();
    }

    private static List<Duty> getDuties(List<DutyConverter> duty) {
        return duty.stream()
                .map(Duty::of)
                .toList();
    }

    @Builder
    private static class CareerConverter {

        private Component career;

        private CareerDetails details;

        private List<DutyConverter> duty;

        @Builder
        private static class CareerDetails {

            private Component position;

            private Component skills;

            private Component careerContent;

            public static CareerDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return CareerDetails.builder()
                        .position(componentMap.get(POSITION))
                        .skills(componentMap.get(SKILL))
                        .careerContent(componentMap.get(CONTENT))
                        .build();
            }

        }

        private static CareerConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            CareerDetails details = CareerDetails.builder()
                    .position(componentMap.get(POSITION))
                    .skills(componentMap.get(SKILL))
                    .careerContent(componentMap.get(CONTENT))
                    .build();

            List<DutyConverter> duties = componentMap.get(DUTY).getComponents().stream()
                    .map(component -> DutyConverter.of(getFlatDuty(component)))
                    .toList();

            return CareerConverter.builder()
                    .career(componentMap.get(COMPANY))
                    .details(details)
                    .duty(duties)
                    .build();
        }

        private static List<Component> getFlatDuty(Component component) {
            Component description = component.getComponents().get(0);

            List<Component> flatDuty = new ArrayList<>();
            flatDuty.add(component);
            flatDuty.add(description);

            return flatDuty;
        }

        private static CareerConverter of(Component component) {
            return CareerConverter.builder()
                    .career(component)
                    .details(CareerDetails.of(component))
                    .duty(getDutyConverters(component))
                    .build();
        }

        private static List<DutyConverter> getDutyConverters(Component component) {
            Map<Property, Component> componentMap = component.getComponents().stream()
                    .collect(toMap(Component::getProperty, identity()));

            return componentMap.get(DUTY).getComponents().stream()
                    .map(DutyConverter::of)
                    .toList();
        }

    }

}
