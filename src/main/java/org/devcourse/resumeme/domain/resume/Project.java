package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
public class Project {

    private String projectName;

    private LocalDate projectStartDate;

    private String projectContent;

    public Project(String projectName, LocalDate projectStartDate, String projectContent) {
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectContent = projectContent;
    }

}
