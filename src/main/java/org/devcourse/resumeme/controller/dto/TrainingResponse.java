package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Training;

import java.time.LocalDate;

public record TrainingResponse(
        String organization,
        String major,
        String degree,
        LocalDate admissionDate,
        LocalDate graduationDate,
        double gpa,
        double maxGpa,
        String explanation
) {

    public TrainingResponse(Training training) {
        this(
                training.getOrganization(),
                training.getMajor(),
                training.getDegree(),
                training.getAdmissionDate(),
                training.getGraduationDate(),
                training.getGpa(),
                training.getMaxGpa(),
                training.getExplanation()
        );
    }

}
