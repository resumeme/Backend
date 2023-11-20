package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.LocalDateUtils;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.devcourse.resumeme.business.resume.domain.Property.COMPANY;
import static org.devcourse.resumeme.business.resume.domain.Property.CONTENT;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.DUTY;
import static org.devcourse.resumeme.business.resume.domain.Property.POSITION;
import static org.devcourse.resumeme.business.resume.domain.Property.SKILL;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career implements Converter {

    private String companyName;

    private String position;

    private List<String> skills;

    private String careerContent;

    private List<Duty> duties;

    private CareerPeriod careerPeriod;

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

    private void validateCareer(String companyName, String position) {
        Validator.check(companyName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(position == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public Career(Map<String, String> component) {
        this(component.get(COMPANY.name()), component.get(POSITION.name()), Arrays.asList((component.get(SKILL.name()) == null ? "" : component.get(SKILL.name())).split(",")), getDuties(component),
                LocalDateUtils.parse(component.get(COMPANY.startDate())), LocalDateUtils.parse(component.get(COMPANY.endDate())), component.get(CONTENT.name()));
    }

    private static List<Duty> getDuties(Map<String, String> component) {
        String dutySize = component.get(DUTY.name());
        if (dutySize == null) {
            return List.of();
        }

        return IntStream.range(0, Integer.parseInt(dutySize))
                .mapToObj(i -> new Duty(component, i))
                .toList();
    }

    @Override
    public Component of(Long resumeId) {
        Component position = new Component(POSITION, this.position, resumeId);
        Component skills = new Component(SKILL, String.join(",", this.skills), resumeId);
        Component careerContent = new Component(CONTENT, this.careerContent, resumeId);

        Component descriptions = new Component(DESCRIPTION, null, null, null, resumeId, List.of(position, skills, careerContent));
        List<Component> duties = getDuties(resumeId);
        Component duty = new Component(DUTY, String.valueOf(duties.size()), null, null, resumeId, duties);

        return new Component(COMPANY, this.companyName, this.careerPeriod.getStartDate(), this.careerPeriod.getEndDate(), resumeId, List.of(descriptions, duty));
    }

    private List<Component> getDuties(Long resumeId) {
        List<Component> result = new ArrayList<>();
        for (int i = 0; i < duties.size(); i++) {
            Duty duty = duties.get(i);

            Component description = new Component(DUTY.name() + DESCRIPTION.name() + i, duty.getDescription(), resumeId);
            result.add(new Component(DUTY.name() + TITLE.name() + i, duty.getTitle(), duty.getStartDate(), duty.getEndDate(), resumeId, List.of(description)));
        }

        return result;
    }

}
