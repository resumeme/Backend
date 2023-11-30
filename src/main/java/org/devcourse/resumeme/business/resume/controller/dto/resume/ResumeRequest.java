package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.Resume;

public record ResumeRequest(
        String title
) {

    public Resume toEntity(Long menteeId) {
        return new Resume(title, menteeId);
    }

}
