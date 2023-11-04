package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ResumeCreateRequest;
import org.devcourse.resumeme.controller.dto.ResumeInfoRequest;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.resume.ResumeInfo;
import org.devcourse.resumeme.global.auth.model.JwtUser;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    private final MenteeService menteeService;

    @PostMapping
    public IdResponse createResume(@AuthenticationPrincipal JwtUser user, @RequestBody ResumeCreateRequest request) {
        Resume resume = request.toEntity(menteeService.getOne(user.id()));
        Long savedId = resumeService.create(resume);

        return new IdResponse(savedId);
    }

    @PatchMapping("/{resumeId}")
    public IdResponse updateResume(@PathVariable Long resumeId, @RequestBody ResumeInfoRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        ResumeInfo resumeInfo = request.toEntity();

        return new IdResponse(resumeService.update(resume, resumeInfo));
    }

}
