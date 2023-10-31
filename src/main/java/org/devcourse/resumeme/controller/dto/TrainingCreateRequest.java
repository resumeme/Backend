package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.resume.Training;

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

    public Training toEntity(Resume resume) {
        return new Training(organization, major, degree, admissionDate, graduationDate, gpa, maxGpa, explanation, resume);
    }

}

