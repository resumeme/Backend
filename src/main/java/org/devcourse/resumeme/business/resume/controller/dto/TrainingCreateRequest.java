package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.Training;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("trainings")
public class TrainingCreateRequest extends ComponentCreateRequest {

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private double gpa;

    private double maxGpa;

    private String explanation;

    public TrainingCreateRequest(
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

    @Override
    public Training toEntity() {
        return new Training(organization, major, degree, admissionDate, graduationDate, gpa, maxGpa, explanation);
    }

}
