package org.devcourse.resumeme.business.resume.controller.career.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("careers")
public class CareerCreateRequest extends ComponentCreateRequest {

    private String companyName;

    private String position;

    private List<String> skills;

    private List<DutyRequest> duties;

    private boolean isCurrentlyEmployed;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;

    public CareerCreateRequest(
            String companyName,
            String position,
            List<String> skills,
            List<DutyRequest> duties,
            boolean isCurrentlyEmployed,
            LocalDate careerStartDate,
            LocalDate endDate,
            String careerContent
    ) {
        this.companyName = companyName;
        this.position = position;
        this.skills = skills;
        this.duties = duties == null ? List.of() : duties;
        this.isCurrentlyEmployed = isCurrentlyEmployed;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

    @Override
    public Career toEntity() {
        List<Duty> duties = this.duties.stream()
                .map(DutyRequest::toEntity)
                .toList();

        return new Career(companyName, position, skills, duties, careerStartDate, endDate, careerContent);
    }

    public record DutyRequest(
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate
    ) {

        public Duty toEntity() {
            return new Duty(title, startDate, endDate, description);
        }

    }

}
