package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ResumeCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public IdResponse createResume(
            /* @AuthenticationPrincipal 인증 유저 */
            @RequestBody ResumeCreateRequest request) {
        Resume resume = request.toEntity(Mentee.builder().build());
        Long savedId = resumeService.create(resume);

        return new IdResponse(savedId);
    }

}
