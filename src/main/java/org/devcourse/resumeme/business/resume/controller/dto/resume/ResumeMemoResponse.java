package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.Resume;

public record ResumeMemoResponse(String memo) {

    public ResumeMemoResponse(Resume resume) {
        this(resume.getMemo());
    }

}
