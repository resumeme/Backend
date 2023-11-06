package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.devcourse.resumeme.common.util.Validator.check;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Training {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "training_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Getter
    private String explanation;

    @Embedded
    private EducationalDetails educationalDetails;

    @Embedded
    private DateDetails dateDetails;

    @Embedded
    private GPADetails gpaDetails;

    public Training(String organization, String major, String degree, LocalDate admissionDate,
                    LocalDate graduationDate, double gpa, double maxGpa, String explanation, Resume resume) {
        this.educationalDetails = new EducationalDetails(organization, major, degree);
        this.dateDetails = new DateDetails(admissionDate, graduationDate);
        this.gpaDetails = new GPADetails(gpa, maxGpa);
        this.explanation = explanation;
        this.resume = resume;
    }

    public String getOrganization() {
        return educationalDetails.getOrganization();
    }

    public String getMajor() {
        return educationalDetails.getMajor();
    }

    public String getDegree() {
        return educationalDetails.getDegree();
    }

    public LocalDate getAdmissionDate() {
        return dateDetails.getAdmissionDate();
    }

    public LocalDate getGraduationDate() {
        return dateDetails.getGraduationDate();
    }

    public double getGpa() {
        return gpaDetails.getGpa();
    }

    public double getMaxGpa() {
        return gpaDetails.getMaxGpa();
    }

}
