package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectResponse;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ProjectController {

    private final ComponentService componentService;

    @GetMapping("/{resumeId}/projects")
    public List<ProjectResponse> getProject(@PathVariable Long resumeId) {
        return componentService.getAll(resumeId).stream()
                .filter(component -> component.isType("PROJECT"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new ProjectResponse(Project.from(component)))
                .toList();
    }

}
