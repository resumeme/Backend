package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project implements Converter {

    private String projectName;

    private Long productionYear;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public Project(String projectName, Long productionYear, String teamMembers, List<String> skills,
            String projectContent, String projectUrl) {
        validateProject(projectName, productionYear);

        this.projectName = projectName;
        this.productionYear = productionYear;
        this.teamMembers = teamMembers;
        this.skills = skills;
        this.projectContent = projectContent;
        this.projectUrl = projectUrl;
    }

    private void validateProject(String projectName, Long productionYear) {
        Validator.check(projectName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(productionYear == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public Project(Map<String, String> component) {
        this(component.get("projectName"), (long) LocalDate.parse(component.get("projectNameStartDate")).getYear(),
                component.get("member"), Arrays.asList(component.get("skills").split(",")),
                component.get("content"), component.get("url"));
    }

    @Override
    public Component of(Long resumeId) {
        Component url = new Component("url", projectUrl, null, null, resumeId, null);
        Component content = new Component("content", projectContent, null, null, resumeId, null);
        Component memberCount = new Component("member", teamMembers, null, null, resumeId, null);
        Component skill = new Component("skills", String.join(",", skills), null, null, resumeId, null);

        return new Component("projectName", this.projectName, LocalDate.of(productionYear.intValue(), 1, 31),
                null, resumeId, List.of(url, content, memberCount, skill));
    }

}
