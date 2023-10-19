package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
public class Training {

    private String trainingName;

    private LocalDate enterDate;

    @Column(nullable = true)
    private LocalDate graduateDate;

    private String trainingContent;

    public Training(String trainingName, LocalDate enterDate, LocalDate graduateDate, String trainingContent) {
        this.trainingName = trainingName;
        this.enterDate = enterDate;
        this.graduateDate = graduateDate;
        this.trainingContent = trainingContent;
    }

}
