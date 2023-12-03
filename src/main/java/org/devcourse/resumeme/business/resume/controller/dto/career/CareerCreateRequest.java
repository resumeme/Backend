package org.devcourse.resumeme.business.resume.controller.dto.career;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;
import org.devcourse.resumeme.business.resume.service.vo.CareerDomainVo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CareerCreateRequest extends ComponentCreateRequest {

    private String companyName;

    private String position;

    private List<String> skills;

    private List<DutyRequest> duties = new ArrayList<>();

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
    public CareerDomainVo toVo() {
        List<Duty> duties = this.duties.stream()
                .map(DutyRequest::toEntity)
                .toList();

        Career career = new Career(companyName, position, skills, duties, careerStartDate, endDate, careerContent);

        return new CareerDomainVo(career);
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
