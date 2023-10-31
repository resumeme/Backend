package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.validate;

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

    private String organization;

    private String major;

    private String degree;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private double gpa;

    private double maxGpa;

    private String explanation;

    public Training(String organization, String major, String degree, LocalDate admissionDate,
                    LocalDate graduationDate, double gpa, double maxGpa, String explanation, Resume resume) {
        validateTraining(organization, major, degree, admissionDate, graduationDate, gpa, maxGpa);

        this.organization = organization;
        this.major = major;
        this.degree = degree;
        this.admissionDate = admissionDate;
        this.graduationDate = graduationDate;
        this.gpa = gpa;
        this.maxGpa = maxGpa;
        this.explanation = explanation;
        this.resume = resume;
    }

    private void validateTraining(String schoolOrOrganization, String major, String degree, LocalDate admissionDate,
                               LocalDate graduationDate, double gpa, double maxGpa) {
        validate(schoolOrOrganization == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(major == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(degree == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(admissionDate == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(graduationDate == null, ExceptionCode.NO_EMPTY_VALUE);

        if (admissionDate.isAfter(graduationDate)) {
            throw new CustomException("TIME_ERROR", "입학 일시는 졸업 일시보다 먼저여야 합니다.");
        }

        if (maxGpa <= gpa) {
            throw new CustomException("GPA_ERROR", "최대 학점은 내 학점보다 커야 합니다.");
        }
    }

}
