package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ActivityRequestDto;
import org.devcourse.resumeme.domain.resume.Activity;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ActivityService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ActivityController {

    private final ActivityService activityService;
    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/activities")
    public IdResponse createActivity(@PathVariable Long resumeId, @RequestBody ActivityRequestDto request) {
        Resume resume = resumeService.getOne(resumeId);
        Activity activity = request.toEntity(resume);

        return new IdResponse(activityService.create(activity));
    }
}

