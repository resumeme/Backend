package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Duty;

import java.time.LocalDate;
import java.util.List;

public record CareerResponse(
        String companyName,
        String position,
        List<String> skills,
        List<DutyResponse> duties,
        boolean isCurrentlyEmployed,
        LocalDate careerStartDate,
        LocalDate endDate,
        String careerContent
) {
    public CareerResponse(Career career) {
        this(
                career.getCompanyName(),
                career.getPosition(),
                career.getSkills(),
                mapDuties(career.getDuties()),
                career.isCurrentlyEmployed(),
                career.getCareerStartDate(),
                career.getEndDate(),
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
