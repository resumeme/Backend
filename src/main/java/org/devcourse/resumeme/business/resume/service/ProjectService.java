package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.business.resume.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devcourse.resumeme.global.exception.ExceptionCode.PROJECT_NOT_FOUND;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Long create(Project project) {
        return projectRepository.save(project).getId();
    }

    public Project getOne(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));
    }

}
