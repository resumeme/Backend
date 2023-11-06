package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.domain.Resume;

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

    public Project toEntity(Resume resume) {
        return new Project(resume, projectName, productionYear, isTeam, teamMembers, skills, projectContent, projectUrl);
    }

}
