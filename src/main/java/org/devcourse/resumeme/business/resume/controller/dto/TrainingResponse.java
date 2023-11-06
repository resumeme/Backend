package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Training;

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
