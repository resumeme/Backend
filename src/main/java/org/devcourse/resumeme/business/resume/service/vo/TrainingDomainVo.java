package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.training.Training;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class TrainingDomainVo implements ComponentDomain {

    private Training training;

    public TrainingDomainVo(Training training) {
        this.training = training;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return training.toComponent(resumeId);
    }

}
