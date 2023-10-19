package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Project {

    private String projectName;

    private LocalDate projectStartDate;

    private String projectContent;

}
