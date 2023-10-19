package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Training {

    private String trainingName;

    private LocalDate enterDate;

    @Column(nullable = true)
    private LocalDate graduateDate;

    private String trainingContent;

}
