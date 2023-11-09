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
import java.util.stream.Collectors;

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

    @Override
    public Component of(Long resumeId) {
        Component url = new Component("url", projectUrl, null, null, resumeId, null);
        Component content = new Component("content", projectContent, null, null, resumeId, null);
        Component memberCount = new Component("member", teamMembers, null, null, resumeId, null);
        Component skill = new Component("skills", String.join(",", skills), null, null, resumeId, null);

        return new Component("projectName", this.projectName, LocalDate.of(productionYear.intValue(), 1, 31),
                null, resumeId, List.of(url, content, memberCount, skill));
    }

    public static Project from(Component component) {
        Map<String, String> collect = component.getComponents().stream()
                .collect(Collectors.toMap(Component::getProperty, Component::getContent));

        return new Project(component.getContent(), (long) component.getStartDate().getYear(), collect.get("member"), Arrays.asList(collect.get("skills").split(",")),
                collect.get("content"), collect.get("url"));
    }

}
