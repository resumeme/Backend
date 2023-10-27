package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.user.User;

import java.time.LocalDate;

public record ResumeCreateRequest(
        String title
) {

    public Resume toEntity(Mentee mentee) {
        return new Resume(title, mentee);
    }

}
