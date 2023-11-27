package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.CONTENT;
import static org.devcourse.resumeme.business.resume.domain.Property.MEMBER;
import static org.devcourse.resumeme.business.resume.domain.Property.PROJECT;
import static org.devcourse.resumeme.business.resume.domain.Property.SKILL;
import static org.devcourse.resumeme.business.resume.domain.Property.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends Converter {

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

    @Builder
    private Project(String projectName, Long productionYear, String teamMembers, List<String> skills,
            String projectContent, String projectUrl, Component component) {
        super(component);
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
    public Component toComponent(Long resumeId) {
        Component url = new Component(URL, projectUrl, resumeId);
        Component content = new Component(CONTENT, projectContent, resumeId);
        Component memberCount = new Component(MEMBER, teamMembers, resumeId);
        Component skill = new Component(SKILL, String.join(",", skills), resumeId);

        return new Component(PROJECT, this.projectName, LocalDate.of(productionYear.intValue(), 1, 31),
                null, resumeId, List.of(url, content, memberCount, skill));
    }

    public static Project of(List<Component> components) {
        ProjectConverter converter = ProjectConverter.of(components);

        return Project.of(converter);
    }

    private static Project of(ProjectConverter converter) {
        return Project.builder()
                .component(converter.project)
                .projectName(converter.project.getContent())
                .productionYear((long) converter.project.getStartDate().getYear())
                .teamMembers(converter.details.members.getContent())
                .skills(Arrays.asList(converter.details.skill.getContent().split(",")))
                .projectContent(converter.details.content.getContent())
                .projectUrl(converter.details.url.getContent())
                .build();
    }

    @Builder
    private static class ProjectConverter {

        private Component project;

        private ProjectDetails details;

        @Builder
        private static class ProjectDetails {

            private Component url;

            private Component content;

            private Component members;

            private Component skill;

        }

        private static ProjectConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            ProjectDetails details = ProjectDetails.builder()
                    .url(componentMap.get(URL))
                    .content(componentMap.get(CONTENT))
                    .members(componentMap.get(MEMBER))
                    .skill(componentMap.get(SKILL))
                    .build();

            return ProjectConverter.builder()
                    .project(componentMap.get(PROJECT))
                    .details(details)
                    .build();
        }

    }

}
