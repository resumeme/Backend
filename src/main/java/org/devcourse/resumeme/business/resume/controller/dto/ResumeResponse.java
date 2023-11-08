package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Resume;

import java.time.LocalDateTime;

public record ResumeResponse(
        Long id,
        String title,
        LocalDateTime modifiedAt
) {

    public ResumeResponse(Resume resume) {
        this(
                resume.getId(),
                resume.getTitle(),
                resume.getLastModifiedDate()
        );
    }
}
