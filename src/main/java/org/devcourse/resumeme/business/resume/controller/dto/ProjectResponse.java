package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Project;

import java.util.List;

@Data
public class ProjectResponse implements ComponentResponse {

    private String projectName;

    private Long productionYear;

    private boolean isTeam;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectResponse(
            String projectName,
            Long productionYear,
            boolean isTeam,
            String teamMembers,
            List<String> skills,
            String projectContent,
            String projectUrl
    ) {
        this.projectName = projectName;
        this.productionYear = productionYear;
        this.isTeam = isTeam;
        this.teamMembers = teamMembers;
        this.skills = skills;
        this.projectContent = projectContent;
        this.projectUrl = projectUrl;
    }

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
