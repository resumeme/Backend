package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public Activity(String activityName, LocalDate startDate, LocalDate endDate, boolean inProgress, String link, String description) {
        validate(activityName == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(startDate == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(endDate == null && !inProgress, ExceptionCode.NO_EMPTY_VALUE);

        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inProgress = inProgress;
        this.link = link;
        this.description = description;
    }

    public String getActivityName() {
        return this.activityName;
    }

}
