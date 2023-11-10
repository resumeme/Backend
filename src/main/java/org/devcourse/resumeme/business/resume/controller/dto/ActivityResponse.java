package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;

import java.time.LocalDate;

@Data
public class ActivityResponse implements ComponentResponse {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityResponse(
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

    public ActivityResponse(Activity activity) {
        this(
                activity.getActivityName(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getEndDate() == null,
                activity.getLink(),
                activity.getDescription()
        );
    }

}
