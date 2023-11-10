package org.devcourse.resumeme.business.resume.controller.career.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;

import java.time.LocalDate;
import java.util.List;

@Data
public class CareerResponse implements ComponentResponse {

    private String companyName;

    private String position;

    private List<String> skills;

    private List<DutyResponse> duties;

    private boolean isCurrentlyEmployed;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;

    public CareerResponse(
            String companyName,
            String position,
            List<String> skills,
            List<DutyResponse> duties,
            boolean isCurrentlyEmployed,
            LocalDate careerStartDate,
            LocalDate endDate,
            String careerContent
    ) {
        this.companyName = companyName;
        this.position = position;
        this.skills = skills;
        this.duties = duties;
        this.isCurrentlyEmployed = isCurrentlyEmployed;
        this.careerStartDate = careerStartDate;
        this.endDate = endDate;
        this.careerContent = careerContent;
    }

    public CareerResponse(Career career) {
        this(
                career.getCompanyName(),
                career.getPosition(),
                career.getSkills(),
                mapDuties(career.getDuties()),
                career.getCareerPeriod().getEndDate() == null,
                career.getCareerPeriod().getStartDate(),
                career.getCareerPeriod().getEndDate(),
                career.getCareerContent()
        );
    }

    private static List<DutyResponse> mapDuties(List<Duty> duties) {
        return duties.stream().map(DutyResponse::new).toList();
    }

    public record DutyResponse(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String description
    ) {

        public DutyResponse(Duty duty) {
            this(
                    duty.getTitle(),
                    duty.getStartDate(),
                    duty.getEndDate(),
                    duty.getDescription()
            );
        }

    }

}
