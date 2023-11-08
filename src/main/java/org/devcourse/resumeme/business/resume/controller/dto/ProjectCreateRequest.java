package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Project;

import java.util.List;

public record ProjectCreateRequest(
        String projectName,
        Long productionYear,
        boolean isTeam,
        String teamMembers,
        List<String> skills,
        String projectContent,
        String projectUrl
) {

    public Project toEntity() {
        return new Project(projectName, productionYear, teamMembers, skills, projectContent, projectUrl);
    }

}
