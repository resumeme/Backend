package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.Training;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonTypeName("trainings")
public class TrainingResponse extends ComponentResponse {

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private Double gpa;

    private Double maxGpa;

    private String explanation;

    public TrainingResponse(String type, Component component) {
        super(type, component);
        Training training = new Training(Converter.from(component));
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
