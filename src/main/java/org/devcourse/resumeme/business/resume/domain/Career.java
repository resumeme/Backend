package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "career_id")
    private Long id;

    @Getter
    private String companyName;

    @Getter
    private String position;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "career_skills")
    @Column(name = "skill")
    private List<String> skills;

    @Getter
    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Duty> duties = new ArrayList<>();

    @Embedded
    private CareerPeriod careerPeriod;

    @Getter
    private String careerContent;


    public Career(String companyName, String position, Resume resume, List<String> skills, List<Duty> duties, boolean isCurrentlyEmployed,
                  LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        CareerPeriod careerPeriod = new CareerPeriod(careerStartDate, endDate, isCurrentlyEmployed);
        validateCareer(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate);

        this.companyName = companyName;
        this.position = position;
        this.resume = resume;
        this.skills = skills;
        this.duties = duties;
        this.careerPeriod = careerPeriod;
        this.careerContent = careerContent;
    }

    private void validateCareer(String companyName, String position, List<String> skills, List<Duty> duties, boolean isCurrentlyEmployed, LocalDate careerStartDate, LocalDate endDate) {
        Validator.check(companyName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(position == null, ExceptionCode.NO_EMPTY_VALUE);
    }

    public boolean isCurrentlyEmployed() {
        return careerPeriod.isCurrentlyEmployed();
    }

    public LocalDate getCareerStartDate() {
        return careerPeriod.getCareerStartDate();
    }

    public LocalDate getEndDate() {
        return careerPeriod.getEndDate();
    }

}
