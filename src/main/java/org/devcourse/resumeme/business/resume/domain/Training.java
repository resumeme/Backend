package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.ComponentUtils.toDouble;
import static org.devcourse.resumeme.business.resume.domain.Property.DEGREE;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.MAJOR;
import static org.devcourse.resumeme.business.resume.domain.Property.MAX_SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.TRAINING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Training implements Converter {

    private String explanation;

    private EducationalDetails educationalDetails;

    private DateDetails dateDetails;

    private GPADetails gpaDetails;

    public Training(String organization, String major, String degree, LocalDate admissionDate,
            LocalDate graduationDate, double gpa, double maxGpa, String explanation) {
        this.educationalDetails = new EducationalDetails(organization, major, degree);
        this.dateDetails = new DateDetails(admissionDate, graduationDate);
        this.gpaDetails = new GPADetails(gpa, maxGpa);
        this.explanation = explanation;
    }

    public Training(Map<Property, Component> components) {
        this.educationalDetails = new EducationalDetails(components.get(TRAINING).getContent(), components.get(MAJOR).getContent(), components.get(DEGREE).getContent());
        this.dateDetails = new DateDetails(components.get(TRAINING).getStartDate(), components.get(TRAINING).getEndDate());
        this.gpaDetails = new GPADetails(toDouble(components.get(SCORE)), toDouble(components.get(MAX_SCORE)));
        this.explanation = components.get(DESCRIPTION).getContent();
    }

    @Override
    public Component of(Long resumeId) {
        Component explanation = new Component(DESCRIPTION, this.explanation, resumeId);
        Component degree = new Component(DEGREE, educationalDetails.getDegree(), resumeId);
        Component major = new Component(MAJOR, educationalDetails.getMajor(), resumeId);
        Component gpa = new Component(SCORE, String.valueOf(gpaDetails.getGpa()), resumeId);
        Component maxGpa = new Component(MAX_SCORE, String.valueOf(gpaDetails.getMaxGpa()), resumeId);

        return new Component(TRAINING, this.educationalDetails.getOrganization(), this.dateDetails.getAdmissionDate(), this.dateDetails.getGraduationDate(),
                resumeId, List.of(explanation, degree, major, gpa, maxGpa));
    }

}
