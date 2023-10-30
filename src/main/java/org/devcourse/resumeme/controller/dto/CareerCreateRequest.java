package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Duty;
import org.devcourse.resumeme.domain.resume.Resume;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());

        return new Career(companyName, Position.valueOf(position), resume, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent);
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
