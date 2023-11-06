package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Resume;

import java.time.LocalDate;

public record ActivityRequestDto(
        String activityName,
        LocalDate startDate,
        LocalDate endDate,
        boolean inProgress,
        String link,
        String description
) {
    public Activity toEntity(Resume resume) {
        return new Activity(activityName, startDate, endDate, inProgress, link, description);
    }
}
