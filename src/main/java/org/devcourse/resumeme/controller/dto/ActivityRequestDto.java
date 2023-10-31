package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Activity;
import org.devcourse.resumeme.domain.resume.Resume;

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
