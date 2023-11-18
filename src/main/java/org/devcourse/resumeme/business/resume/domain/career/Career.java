package org.devcourse.resumeme.business.resume.domain.career;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
        this(component.get("company"), component.get("position"), Arrays.asList(component.get("skill").split(",")),
                getDuties(component), LocalDate.parse(component.get("companyStartDate")), LocalDate.parse(component.get("companyEndDate")), component.get("careerContent"));
    }

    private static List<Duty> getDuties(Map<String, String> component) {
        return IntStream.range(0, Integer.parseInt(component.get("duty")))
                .mapToObj(i -> new Duty(component, "dutyTitle" + i))
                .toList();
    }

    @Override
    public Component of(Long resumeId) {
        Component position = new Component("position", this.position, null, null, resumeId, null);
        Component skills = new Component("skill", String.join(",", this.skills), null, null, resumeId, null);
        Component careerContent = new Component("careerContent", this.careerContent, null, null, resumeId, null);

        Component descriptions = new Component("description", "description", null, null, resumeId, List.of(position, skills, careerContent));
        List<Component> duties = getDuties(resumeId);
        Component duty = new Component("duty", String.valueOf(duties.size()), null, null, resumeId, duties);

        return new Component("company", this.companyName, this.careerPeriod.getStartDate(), this.careerPeriod.getEndDate(), resumeId, List.of(descriptions, duty));
    }

    private List<Component> getDuties(Long resumeId) {
        List<Component> result = new ArrayList<>();
        for (int i = 0; i < duties.size(); i++) {
            Duty duty = duties.get(i);

            Component description = new Component("dutyDescription" + i, duty.getTitle(), duty.getStartDate(), duty.getEndDate(), resumeId, null);
            result.add(new Component("dutyTitle" + i, duty.getTitle(), duty.getStartDate(), duty.getEndDate(), resumeId, List.of(description)));
        }

        return result;
    }

}
