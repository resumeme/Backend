package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;

public record ResumeRequest(
        String title
) {

    public Resume toEntity(Mentee mentee) {
        return new Resume(title, mentee);
    }

}
