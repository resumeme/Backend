package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Activity;

import java.time.LocalDate;

public record ActivityResponse(
        String activityName,
        LocalDate startDate,
        LocalDate endDate,
        boolean inProgress,
        String link,
        String description
) {
    public ActivityResponse(Activity activity) {
        this(
                activity.getActivityName(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.isInProgress(),
                activity.getLink(),
                activity.getDescription()
        );
    }
}
