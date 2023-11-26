package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.ComponentUtils.toList;
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

    public Career(Map<Property, Component> component) {
        this.companyName = component.get(COMPANY).getContent();
        this.position = component.get(POSITION).getContent();
        this.skills = toList(component.get(SKILL));
        this.duties = getDuties(component.get(DUTY));
        this.careerPeriod = new CareerPeriod(component.get(COMPANY).getStartDate(), component.get(COMPANY).getEndDate());
        this.careerContent = component.get(CONTENT).getContent();
    }

    private static List<Duty> getDuties(Component component) {
        return component.getComponents().stream()
                .map(Duty::new)
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
        for (Duty duty : duties) {
            Component description = new Component(DESCRIPTION, duty.getDescription(), resumeId);
            result.add(new Component(TITLE, duty.getTitle(), duty.getStartDate(), duty.getEndDate(), resumeId, List.of(description)));
        }

        return result;
    }

}
