package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.util.List;

import static org.devcourse.resumeme.common.util.Validator.check;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Getter
    private String projectName;

    @Getter
    private Long productionYear;

    @Getter
    private boolean isTeam;

    private String TeamMembers;

    @Getter
    @ElementCollection
    @CollectionTable(name = "career_skills")
    @Column(name = "skill")
    private List<String> skills;

    @Getter
    private String projectContent;

    @Getter
    private String projectUrl;

    public Project(Resume resume, String projectName, Long productionYear, boolean isTeam, String teamMembers, List<String> skills,
                   String projectContent, String projectUrl) {
        validateProject(projectName, productionYear);

        this.resume = resume;
        this.projectName = projectName;
        this.productionYear = productionYear;
        this.isTeam = isTeam;
        if (isTeam) {
            this.TeamMembers = teamMembers;
        }
        this.skills = skills;
        this.projectContent = projectContent;
        this.projectUrl = projectUrl;
    }

    private void validateProject(String projectName, Long productionYear) {
        Validator.check(projectName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(productionYear == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public String getTeamMembers() {
        return isTeam ? TeamMembers : null;
    }

}
