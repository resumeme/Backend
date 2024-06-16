package org.devcourse.resumeme.business.resume.controller.dto.training;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.training.Training;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class TrainingResponse extends ComponentResponse {

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private Double gpa;

    private Double maxGpa;

    private String explanation;

    public TrainingResponse(Training training) {
        super(training.getComponentInfo());
        this.organization = training.getEducationalDetails().getOrganization();
        this.major = training.getEducationalDetails().getMajor();
        this.degree = training.getEducationalDetails().getDegree();
        this.admissionDate = training.getDateDetails().getAdmissionDate();
        this.graduationDate = training.getDateDetails().getGraduationDate();
        this.gpa = training.getGpaDetails().getGpa();
        this.maxGpa = training.getGpaDetails().getMaxGpa();
        this.explanation = training.getExplanation();
    }

}
