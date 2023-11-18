package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        this(component.get("training"), component.get("major"), component.get("degree"), LocalDate.parse(component.get("trainingStartDate")),
                LocalDate.parse(component.get("trainingEndDate")), Double.parseDouble(component.get("gpa")),
                Double.parseDouble(component.get("maxGpa")), component.get("explanation"));
    }

    @Override
    public Component of(Long resumeId) {
        Component explanation = new Component("explanation", this.explanation, null, null, resumeId, null);
        Component degree = new Component("degree", educationalDetails.getDegree(), null, null, resumeId, null);
        Component major = new Component("major", educationalDetails.getMajor(), null, null, resumeId, null);
        Component gpa = new Component("gpa", String.valueOf(gpaDetails.getGpa()), null, null, resumeId, null);
        Component maxGpa = new Component("maxGpa", String.valueOf(gpaDetails.getMaxGpa()), null, null, resumeId, null);

        return new Component("training", this.educationalDetails.getOrganization(), this.dateDetails.getAdmissionDate(), this.dateDetails.getGraduationDate(),
                resumeId, List.of(explanation, degree, major, gpa, maxGpa));
    }

}
