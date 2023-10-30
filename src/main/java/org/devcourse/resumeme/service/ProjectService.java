package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Project;
import org.devcourse.resumeme.repository.CareerRepository;
import org.devcourse.resumeme.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Long create(Project project) {
        return projectRepository.save(project).getId();
    }

}
