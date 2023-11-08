package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Activity;

import java.time.LocalDate;

public record ActivityRequestDto(
        String activityName,
        LocalDate startDate,
        LocalDate endDate,
        boolean inProgress,
        String link,
        String description
) {
    public Activity toEntity() {
        return new Activity(activityName, startDate, endDate, link, description);
    }
}
