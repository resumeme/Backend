package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.ActivityService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityRequestDto;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{resumeId}/activities")
    public List<ActivityResponse> getActivity(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<Activity> activities = resume.getActivity();

        return activities.stream()
                .map(ActivityResponse::new)
                .toList();
    }

}
