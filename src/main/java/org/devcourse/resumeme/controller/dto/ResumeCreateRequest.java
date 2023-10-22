package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;

import java.time.LocalDate;

public record ResumeCreateRequest(
        String title,
        Position position,
        String introduce,
        Training training,
        Career career,
        Project project,
        String email,
        String githubAddress,
        String blogAddress,
        String phoneNumber
) {

    public Resume toEntity(Mentee mentee) {
        return new Resume(title, mentee, introduce);
    }

    public record Training(
            String trainingName,
            LocalDate enterDate,
            LocalDate graduateDate,
            String trainingContent
    ) {
    }

    public record Career(
            String companyName,
            String employmentType,
            LocalDate careerStartDate,
            LocalDate endDate,
            String careerContent
    ) {
    }

    public record Project(
            String projectName,
            LocalDate projectStartDate,
            String projectContent
    ) {
    }

}
