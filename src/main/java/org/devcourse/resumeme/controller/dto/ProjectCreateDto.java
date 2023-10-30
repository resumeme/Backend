package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Project;
import org.devcourse.resumeme.domain.resume.Resume;

import java.util.List;

public record ProjectCreateDto(
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
