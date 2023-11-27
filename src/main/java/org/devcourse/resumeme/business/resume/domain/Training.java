package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.model.Components;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;

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

    public Training(Components components) {
        this.educationalDetails = new EducationalDetails(components.getContent(TRAINING), components.getContent(MAJOR), components.getContent(DEGREE));
        this.dateDetails = new DateDetails(components.getStartDate(TRAINING), components.getEndDate(TRAINING));
        this.gpaDetails = new GPADetails(components.toDouble(SCORE), components.toDouble(MAX_SCORE));
        this.explanation = components.getContent(DESCRIPTION);
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
