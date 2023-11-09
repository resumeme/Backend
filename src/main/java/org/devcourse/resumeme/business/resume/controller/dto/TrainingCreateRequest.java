package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Training;

import java.time.LocalDate;

public record TrainingCreateRequest(
        String organization,
        String major,
        String degree,
        LocalDate admissionDate,
        LocalDate graduationDate,
        double gpa,
        double maxGpa,
        String explanation
) {

    public Training toEntity() {
        return new Training(organization, major, degree, admissionDate, graduationDate, gpa, maxGpa, explanation);
    }

}
