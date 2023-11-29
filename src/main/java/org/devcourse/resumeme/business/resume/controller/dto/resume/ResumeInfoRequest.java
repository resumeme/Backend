package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.ResumeInfo;

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
