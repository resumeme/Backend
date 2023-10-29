package org.devcourse.resumeme.domain.resume;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.List;

import static org.devcourse.resumeme.common.util.Validator.validate;

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
    private List<Skill> skills;

    @ElementCollection
    @CollectionTable(name = "career_duties")
    private List<Duty> duties;

    private boolean isCurrentlyEmployed;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;


    public Career(String companyName, Position position, List<Skill> skills, List<Duty> duties, boolean isCurrentlyEmployed,
                  LocalDate careerStartDate, LocalDate endDate, String careerContent) {
        validateCareer(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate);

        this.companyName = companyName;
        this.position = position;
        this.skills = skills;
        this.duties = duties;
        this.isCurrentlyEmployed = isCurrentlyEmployed;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

    private void validateCareer(String companyName, Position position, List<Skill> skills, List<Duty> duties, boolean isCurrentlyEmployed, LocalDate careerStartDate, LocalDate endDate) {
        validate(companyName == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(position == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(skills.isEmpty(), ExceptionCode.NO_EMPTY_VALUE);
        validate(duties.isEmpty(), ExceptionCode.NO_EMPTY_VALUE);
        validate(careerStartDate == null, ExceptionCode.NO_EMPTY_VALUE);
        if (isCurrentlyEmployed) {
            validate(endDate == null, ExceptionCode.NO_EMPTY_VALUE);
        }

        if (careerStartDate.isAfter(endDate)) {
            throw new CustomException("TIME_ERROR", "시작일은 종료일보다 먼저여야 합니다.");
        }
    }

}
