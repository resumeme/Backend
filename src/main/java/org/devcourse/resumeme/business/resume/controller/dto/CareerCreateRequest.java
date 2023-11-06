package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Career;
import org.devcourse.resumeme.business.resume.domain.Duty;
import org.devcourse.resumeme.business.resume.domain.Resume;

import java.time.LocalDate;
import java.util.List;

public record CareerCreateRequest(
        String companyName,
        String position,
        List<String> skills,
        List<DutyRequest> duties,
        boolean isCurrentlyEmployed,
        LocalDate careerStartDate,
        LocalDate endDate,
        String careerContent
) {
    public Career toEntity(Resume resume) {
        List<Duty> duties = this.duties.stream()
                .map(DutyRequest::toEntity)
                .toList();

        return new Career(companyName, position, resume, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent);
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
