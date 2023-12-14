package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeCopier implements ResumeProvider {

    private final ResumeService resumeService;

    private final ComponentService componentService;

    @Override
    public Long copy(Long resumeId) {
        Long copyResumeId = resumeService.copyResume(resumeId);
        componentService.copy(resumeId, copyResumeId);

        return copyResumeId;
    }

}
