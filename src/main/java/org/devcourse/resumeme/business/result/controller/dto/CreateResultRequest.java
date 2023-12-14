package org.devcourse.resumeme.business.result.controller.dto;

import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.result.domain.ResultNotice;

public record CreateResultRequest(Long resumeId, String content) {

    public ResultNotice toEntity(Resume resume) {
        return new ResultNotice(content, resume);
    }

}
