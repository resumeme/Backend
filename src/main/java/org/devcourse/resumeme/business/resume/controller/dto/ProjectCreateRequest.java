package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.Project;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("projects")
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
    public Project toEntity() {
        if (teamMembers == null) {
            teamMembers = "";
        }

        return new Project(projectName, productionYear, teamMembers, skills, projectContent, projectUrl);
    }

}
