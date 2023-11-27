package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Project;

import java.util.List;

@Getter
public class ProjectResponse extends ComponentResponse {

    private String projectName;

    private Long productionYear;

    private boolean isTeam;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectResponse(Project project) {
        super(project);
        this.projectName = project.getProjectName();
        this.productionYear = project.getProductionYear();
        this.isTeam = !project.getTeamMembers().equals("");
        this.teamMembers = project.getTeamMembers();
        this.skills = project.getSkills();
        this.projectContent = project.getProjectContent();
        this.projectUrl = project.getProjectUrl();
    }

}
