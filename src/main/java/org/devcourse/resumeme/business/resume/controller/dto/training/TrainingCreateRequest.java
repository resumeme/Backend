package org.devcourse.resumeme.business.resume.controller.dto.training;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.training.Training;
import org.devcourse.resumeme.business.resume.service.vo.TrainingDomainVo;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TrainingCreateRequest extends ComponentCreateRequest {

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private Double gpa;

    private Double maxGpa;

    private String explanation;

    public TrainingCreateRequest(
            String organization,
            String major,
            String degree,
            LocalDate admissionDate,
            LocalDate graduationDate,
            Double gpa,
            Double maxGpa,
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
    public TrainingDomainVo toVo() {
        Training training = new Training(organization, major, degree, admissionDate, graduationDate, gpa, maxGpa, explanation);

        return new TrainingDomainVo(training);
    }

}
