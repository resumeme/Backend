package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.devcourse.resumeme.business.resume.domain.BlockType.CAREER;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career implements Converter {

    private String companyName;

    private String position;

    private List<String> skills;

    private String careerContent;

    private List<Duty> duties;

    private CareerPeriod careerPeriod;

    private BlockType type = CAREER;

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

    @Override
    public Component of(Long resumeId) {
        Component position = new Component("position", this.position, null, null, resumeId, null);
        Component skills = new Component("skill", String.join(",", this.skills), null, null, resumeId, null);
        Component careerContent = new Component("careerContent", this.careerContent, null, null, resumeId, null);

        Component descriptions = new Component("description", "description", null, null, resumeId, List.of(position, skills, careerContent));
        Component duty = new Component("duty", "duty", null, null, resumeId, getDuties(resumeId));

        return new Component("company", this.companyName, this.careerPeriod.getStartDate(), this.careerPeriod.getEndDate(), resumeId, List.of(descriptions, duty));
    }

    private List<Component> getDuties(Long resumeId) {
        return duties.stream()
                .map(duty -> new Component(duty.getTitle(), duty.getDescription(), duty.getStartDate(), duty.getEndDate(), resumeId, null))
                .toList();
    }

    public static Career from(Component careerComponent) {
        Map<String, String> description = new HashMap<>();
        List<Duty> duties = new ArrayList<>();

        for (Component component : careerComponent.getComponents()) {
            if (component.getProperty().equals("description")) {
                description = component.getComponents().stream()
                        .collect(Collectors.toMap(Component::getProperty, Component::getContent));
            } else if (component.getProperty().equals("duty")) {
                duties = component.getComponents().stream()
                        .map(component1 -> new Duty(component1.getProperty(), component1.getStartDate(), component1.getEndDate(), component1.getContent()))
                        .toList();
            }
        }

        return new Career(careerComponent.getProperty(), description.get("position"),
                Arrays.asList(description.get("skill").split(",")), duties, careerComponent.getStartDate(),
                careerComponent.getEndDate(), description.get("careerContent"));
    }

}
