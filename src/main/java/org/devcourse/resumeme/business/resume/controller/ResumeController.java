package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeInfoRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeResponse;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.ResumeInfo;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    private final MenteeService menteeService;

    @PostMapping
    public IdResponse createResume(@AuthenticationPrincipal JwtUser user, @RequestBody ResumeRequest request) {
        Resume resume = request.toEntity(menteeService.getOne(user.id()));
        Long savedId = resumeService.create(resume);

        return new IdResponse(savedId);
    }

    @PatchMapping("/{resumeId}")
    public IdResponse updateResume(@PathVariable Long resumeId, @RequestBody ResumeInfoRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        ResumeInfo resumeInfo = request.toEntity();

        return new IdResponse(resumeService.updateResumeInfo(resume, resumeInfo));
    }

    @PatchMapping("/{resumeId}/title")
    public IdResponse updateResumeTitle(@PathVariable Long resumeId, @RequestBody ResumeRequest request) {
        Resume resume = resumeService.getOne(resumeId);

        return new IdResponse(resumeService.updateTitle(resume, request.title()));
    }

    @PatchMapping("/{resumeId}/link")
    public IdResponse updateResumeReferenceLink(@PathVariable Long resumeId, @RequestBody ResumeLinkRequest request) {
        Resume resume = resumeService.getOne(resumeId);

        return new IdResponse(resumeService.updateReferenceLink(resume, request.toEntity()));
    }

    @GetMapping("/{resumeId}/link")
    public ResumeLinkResponse getupdateResumeReferenceLink(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        ReferenceLink referenceLink = resume.getReferenceLink();

        return new ResumeLinkResponse(referenceLink);
    }

    @GetMapping
    public List<ResumeResponse> getAll(@AuthenticationPrincipal JwtUser user) {
        return resumeService.getAllByMenteeId(user.id()).stream()
                .map(ResumeResponse::new)
                .toList();
    }

}
