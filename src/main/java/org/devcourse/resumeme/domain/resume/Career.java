package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
public class Career {

    private String companyName;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;

    public Career(String companyName, EmploymentType employmentType, LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        this.companyName = companyName;
        this.employmentType = employmentType;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

}
