package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.Activity;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("activities")
public class ActivityCreateRequest extends ComponentCreateRequest {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityCreateRequest(
            String activityName,
            LocalDate startDate,
            LocalDate endDate,
            boolean inProgress,
            String link,
            String description
    ) {
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inProgress = inProgress;
        this.link = link;
        this.description = description;
    }

    @Override
    public Activity toEntity() {
        return new Activity(activityName, startDate, endDate, link, description);
    }

}
