package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
public class Career {

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Position careerPosition;

    private LocalDate careerStartDate;

    @Column(nullable = true)
    private LocalDate endDate;

    private String careerContent;

    public Career(String companyName, Position careerPosition, LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        this.companyName = companyName;
        this.careerPosition = careerPosition;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

}
