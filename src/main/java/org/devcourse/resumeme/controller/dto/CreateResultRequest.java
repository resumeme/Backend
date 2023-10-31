package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.result.ResultNotice;
import org.devcourse.resumeme.domain.resume.Resume;

public record CreateResultRequest(Long resumeId, String content) {

    public ResultNotice toEntity(Resume resume) {
        return new ResultNotice(content, resume);
    }

}
