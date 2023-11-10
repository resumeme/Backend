package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ActivityController {

    private final ComponentService componentService;

    @GetMapping("/{resumeId}/activities")
    public List<ActivityResponse> getActivity(@PathVariable Long resumeId) {
        return componentService.getAll(resumeId).stream()
                .filter(component -> component.isType("ACTIVITY"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new ActivityResponse(Activity.from(component)))
                .toList();
    }

}
