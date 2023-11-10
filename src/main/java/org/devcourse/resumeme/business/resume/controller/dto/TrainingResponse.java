package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Training;

import java.time.LocalDate;

@Data
public class TrainingResponse implements ComponentResponse {

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private double gpa;

    private double maxGpa;

    private String explanation;

    public TrainingResponse(
            String organization,
            String major,
            String degree,
            LocalDate admissionDate,
            LocalDate graduationDate,
            double gpa,
            double maxGpa,
            String explanation
    ) {
        this.organization = organization;
        this.major = major;
        this.degree = degree;
        this.admissionDate = admissionDate;
        this.graduationDate = graduationDate;
        this.gpa = gpa;
        this.maxGpa = maxGpa;
        this.explanation = explanation;
    }

    public TrainingResponse(Training training) {
        this(
                training.getEducationalDetails().getOrganization(),
                training.getEducationalDetails().getMajor(),
                training.getEducationalDetails().getDegree(),
                training.getDateDetails().getAdmissionDate(),
                training.getDateDetails().getGraduationDate(),
                training.getGpaDetails().getGpa(),
                training.getGpaDetails().getMaxGpa(),
                training.getExplanation()
        );
    }

}
