package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Project {

    private String name;

    private LocalDate startDate;

    private String content;

}
