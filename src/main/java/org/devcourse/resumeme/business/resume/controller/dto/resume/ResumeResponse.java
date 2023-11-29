package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.Resume;

import java.time.LocalDateTime;

public record ResumeResponse(
        Long id,
        String title,
        LocalDateTime modifiedAt,
        String position,
        String memo
) {

    public ResumeResponse(Resume resume) {
        this(
                resume.getId(),
                resume.getTitle(),
                resume.getLastModifiedDate(),
                resume.getResumeInfo().getPosition(),
                resume.getMemo()
        );
    }
}
