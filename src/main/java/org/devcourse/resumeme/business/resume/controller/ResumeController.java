package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.resume.BasicResumeInfo;
import org.devcourse.resumeme.business.resume.controller.dto.resume.ResumeRequest;
import org.devcourse.resumeme.business.resume.controller.dto.resume.ResumeResponse;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.service.UserProvider;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final UserProvider userProvider;

    @PostMapping
    public IdResponse createResume(@AuthenticationPrincipal JwtUser user, @RequestBody ResumeRequest request) {
        Resume resume = request.toEntity(user.id());
        Long savedId = resumeService.create(resume);

        return new IdResponse(savedId);
    }

    @GetMapping("/{resumeId}/basic")
    public BasicResumeInfo getBasicInformation(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        UserResponse user = userProvider.getOne(resume.getMenteeId());

        return new BasicResumeInfo(resume, user);
    }

    @GetMapping
    public List<ResumeResponse> getAll(@AuthenticationPrincipal JwtUser user) {
        return resumeService.getAllByMenteeId(user.id()).stream()
                .map(ResumeResponse::new)
                .toList();
    }

    @DeleteMapping("/{resumeId}")
    public void deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
    }

}
