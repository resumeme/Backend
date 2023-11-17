package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Project;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectResponse extends ComponentResponse {

    private String projectName;

    private Long productionYear;

    private boolean isTeam;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectResponse(
            Long id, String projectName,
            Long productionYear,
            boolean isTeam,
            String teamMembers,
            List<String> skills,
            String projectContent,
            String projectUrl
    ) {
        super(id);
        this.projectName = projectName;
        this.productionYear = productionYear;
        this.isTeam = isTeam;
        this.teamMembers = teamMembers;
        this.skills = skills;
        this.projectContent = projectContent;
        this.projectUrl = projectUrl;
    }

    public ProjectResponse(Project project, Long id) {
        this(
                id,
                project.getProjectName(),
                project.getProductionYear(),
                !project.getTeamMembers().equals(""),
                project.getTeamMembers(),
                project.getSkills(),
                project.getProjectContent(),
                project.getProjectUrl()
        );
    }

}
