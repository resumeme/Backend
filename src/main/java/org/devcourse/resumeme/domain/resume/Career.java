package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.devcourse.resumeme.common.util.Validator.check;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "career_id")
    private Long id;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ElementCollection
    @CollectionTable(name = "career_skills")
    @Column(name = "skill")
    private List<String> skills;

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Duty> duties = new ArrayList<>();

    private boolean isCurrentlyEmployed;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;


    public Career(String companyName, Position position, Resume resume, List<String> skills, List<Duty> duties, boolean isCurrentlyEmployed,
                  LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        validateCareer(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate);

        this.companyName = companyName;
        this.position = position;
        this.resume = resume;
        this.skills = skills;
        this.duties = duties;
        this.isCurrentlyEmployed = isCurrentlyEmployed;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

    private void validateCareer(String companyName, Position position, List<String> skills, List<Duty> duties, boolean isCurrentlyEmployed, LocalDate careerStartDate, LocalDate endDate) {
        Validator.check(companyName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(position == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(skills.isEmpty(), ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(duties.isEmpty(), ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(careerStartDate == null, ExceptionCode.NO_EMPTY_VALUE);
        if (isCurrentlyEmployed) {
            Validator.check(endDate == null, ExceptionCode.NO_EMPTY_VALUE);
        }

        if (careerStartDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
