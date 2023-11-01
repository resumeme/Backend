package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Project;

import java.util.List;

public record ProjectResponse(
        String projectName,
        Long productionYear,
        boolean isTeam,
        String teamMembers,
        List<String> skills,
        String projectContent,
        String projectUrl
) {
    public ProjectResponse(Project project) {
        this(
                project.getProjectName(),
                project.getProductionYear(),
                project.isTeam(),
                project.getTeamMembers(),
                project.getSkills(),
                project.getProjectContent(),
                project.getProjectUrl()
        );
    }
}
