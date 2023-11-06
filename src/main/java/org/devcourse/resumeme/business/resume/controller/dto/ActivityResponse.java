package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Activity;

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
