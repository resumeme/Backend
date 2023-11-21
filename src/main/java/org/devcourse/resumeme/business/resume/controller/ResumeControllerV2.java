package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.v2.ResumeUpdateRequest;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.resume.service.v2.ResumeUpdateVo;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/resumes")
public class ResumeControllerV2 {

    private final ResumeService resumeService;

    @PatchMapping("/{resumeId}")
    public IdResponse updateResume(@PathVariable Long resumeId, @RequestBody ResumeUpdateRequest request) {
        ResumeUpdateVo update = request.toVo();
        resumeService.update(resumeId, update);

        return new IdResponse(resumeId);
    }

}
