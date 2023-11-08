package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Project;

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
                project.getTeamMembers().equals("1"),
                project.getTeamMembers(),
                project.getSkills(),
                project.getProjectContent(),
                project.getProjectUrl()
        );
    }
}
