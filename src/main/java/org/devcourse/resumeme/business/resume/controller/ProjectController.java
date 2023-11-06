package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.ProjectService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectResponse;
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
public class ProjectController {

    private final ProjectService projectService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/projects")
    public IdResponse createProject(@PathVariable Long resumeId, @RequestBody ProjectCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Project project = request.toEntity(resume);

        return new IdResponse(projectService.create(project));
    }

    @GetMapping("/{resumeId}/projects")
    public List<ProjectResponse> getProject(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<Project> projects = resume.getProject();

        return projects.stream()
                .map(ProjectResponse::new)
                .toList();
    }

}
