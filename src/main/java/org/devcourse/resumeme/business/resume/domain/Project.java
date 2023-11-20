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

import static org.devcourse.resumeme.business.resume.domain.LocalDateUtils.parse;
import static org.devcourse.resumeme.business.resume.domain.Property.CONTENT;
import static org.devcourse.resumeme.business.resume.domain.Property.MEMBER;
import static org.devcourse.resumeme.business.resume.domain.Property.PROJECT;
import static org.devcourse.resumeme.business.resume.domain.Property.SKILL;
import static org.devcourse.resumeme.business.resume.domain.Property.URL;

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
        this(component.get(PROJECT.name()), (long) parse(component.get(PROJECT.startDate())).getYear(),
                component.get(MEMBER.name()), Arrays.asList((component.get(SKILL.name()) == null ? "" : component.get(SKILL.name())).split(",")),
                component.get(CONTENT.name()), component.get(URL.name()));
    }

    @Override
    public Component of(Long resumeId) {
        Component url = new Component(URL, projectUrl, resumeId);
        Component content = new Component(CONTENT, projectContent, resumeId);
        Component memberCount = new Component(MEMBER, teamMembers, resumeId);
        Component skill = new Component(SKILL, String.join(",", skills), resumeId);

        return new Component(PROJECT, this.projectName, LocalDate.of(productionYear.intValue(), 1, 31),
                null, resumeId, List.of(url, content, memberCount, skill));
    }

}
