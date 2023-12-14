package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class ProjectDomainVo implements ComponentDomain {

    private Project project;

    public ProjectDomainVo(Project project) {
        this.project = project;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return project.toComponent(resumeId);
    }

}
