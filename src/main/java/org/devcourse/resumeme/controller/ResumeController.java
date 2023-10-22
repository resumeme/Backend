package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ResumeCreateRequest;
import org.devcourse.resumeme.controller.dto.ResumeCreateResponse;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final ResumeService resumeService;

    private final MenteeService menteeService;

    @PostMapping("/{menteeId}")
    public Long createResume(
            @PathVariable Long menteeId,
            @RequestBody ResumeCreateRequest request) {

        Mentee mentee = menteeService.getOne(menteeId);
        Resume resume = request.toEntity(mentee);

        Long savedId = resumeService.create(resume);

        return savedId;
    }

}
