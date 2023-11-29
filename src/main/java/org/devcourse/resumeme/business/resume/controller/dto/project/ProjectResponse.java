package org.devcourse.resumeme.business.resume.controller.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.project.Project;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class ProjectResponse extends ComponentResponse {

    private String projectName;

    private Long productionYear;

    private boolean team;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectResponse(Project project) {
        super(project.getComponentInfo());
        this.projectName = project.getProjectName();
        this.productionYear = project.getProductionYear();
        this.team = !project.getTeamMembers().equals("");
        this.teamMembers = project.getTeamMembers();
        this.skills = project.getSkills();
        this.projectContent = project.getProjectContent();
        this.projectUrl = project.getProjectUrl();
    }

}
