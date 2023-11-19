package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Resume;

public record ResumeMemoResponse(String memo) {

    public ResumeMemoResponse(Resume resume) {
        this(resume.getMemo());
    }

}
