package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ProjectCreateDto;
import org.devcourse.resumeme.domain.resume.Project;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ProjectService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ProjectController {

    private final ProjectService projectService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/projects")
    public IdResponse createProject(@PathVariable Long resumeId, @RequestBody ProjectCreateDto request) {
        Resume resume = resumeService.getOne(resumeId);
        Project project = request.toEntity(resume);

        return new IdResponse(projectService.create(project));
    }

}
