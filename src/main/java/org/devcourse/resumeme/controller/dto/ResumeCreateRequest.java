package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;

public record ResumeCreateRequest(
        String title
) {

    public Resume toEntity(Mentee mentee) {
        return new Resume(title, mentee);
    }

}
