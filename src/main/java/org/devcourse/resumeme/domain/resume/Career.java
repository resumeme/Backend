package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDate;

@Embeddable
public class Career {

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Position careerPosition;

    private LocalDate careerStartDate;

    @Column(nullable = true)
    private LocalDate endDate;

    private String careerContent;

}
