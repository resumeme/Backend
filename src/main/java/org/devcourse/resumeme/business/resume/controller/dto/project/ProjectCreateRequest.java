package org.devcourse.resumeme.business.resume.controller.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.business.resume.service.vo.ProjectDomainVo;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectCreateRequest extends ComponentCreateRequest {

    private String projectName;

    private Long productionYear;

    private boolean isTeam;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectCreateRequest(
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

    @Override
    public ProjectDomainVo toVo() {
        if (teamMembers == null) {
            teamMembers = "";
        }

        Project project = new Project(projectName, productionYear, teamMembers, skills, projectContent, projectUrl);

        return new ProjectDomainVo(project);
    }

}
