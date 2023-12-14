package org.devcourse.resumeme.business.resume.domain.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
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
import static org.devcourse.resumeme.business.resume.domain.Property.CONTENT;
import static org.devcourse.resumeme.business.resume.domain.Property.MEMBER;
import static org.devcourse.resumeme.business.resume.domain.Property.PROJECT;
import static org.devcourse.resumeme.business.resume.domain.Property.SKILL;
import static org.devcourse.resumeme.business.resume.domain.Property.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    private String projectName;

    private Long productionYear;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    private ComponentInfo componentInfo;

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
        validateProject(projectName, productionYear);

        this.projectName = projectName;
        this.productionYear = productionYear;
        this.teamMembers = teamMembers;
        this.skills = skills;
        this.projectContent = projectContent;
        this.projectUrl = projectUrl;
        this.componentInfo = new ComponentInfo(component);
    }

    private void validateProject(String projectName, Long productionYear) {
        Validator.check(projectName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(productionYear == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public Component toComponent(Long resumeId) {
        Component url = new Component(URL, projectUrl, resumeId);
        Component content = new Component(CONTENT, projectContent, resumeId);
        Component memberCount = new Component(MEMBER, teamMembers, resumeId);
        Component skill = new Component(SKILL, String.join(",", skills), resumeId);

        return new Component(PROJECT, this.projectName, LocalDate.of(productionYear.intValue(), 1, 31),
                null, resumeId, List.of(url, content, memberCount, skill));
    }

    public static List<Project> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(ProjectConverter::of)
                .map(Project::of)
                .toList();
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

            public static ProjectDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return ProjectDetails.builder()
                        .url(componentMap.get(URL))
                        .content(componentMap.get(CONTENT))
                        .members(componentMap.get(MEMBER))
                        .skill(componentMap.get(SKILL))
                        .build();
            }

        }

        private static ProjectConverter of(Component component) {
            return ProjectConverter.builder()
                    .project(component)
                    .details(ProjectDetails.of(component))
                    .build();
        }

    }

}
