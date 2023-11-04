package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.resume.ResumeInfo;

import java.util.List;

public record ResumeInfoRequest(
        String position,
        List<String> skills,
        String introduce
) {

    public ResumeInfo toEntity() {
        return new ResumeInfo(position, skills, introduce);
    }
}
