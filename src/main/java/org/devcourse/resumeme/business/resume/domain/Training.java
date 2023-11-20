package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.LocalDateUtils.parse;
import static org.devcourse.resumeme.business.resume.domain.Property.*;

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

    public Training(Map<String, String> component) {
        this(component.get(TRAINING.name()), component.get(MAJOR.name()), component.get(DEGREE.name()),
                parse(component.get(TRAINING.startDate())), parse(component.get(TRAINING.endDate())),
                Double.parseDouble(component.get(SCORE.name()) == null ? "0" : component.get(SCORE.name())),
                Double.parseDouble(component.get(MAX_SCORE.name()) == null ? "0" : component.get(MAX_SCORE.name())),
                component.get(DESCRIPTION.name()));
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
