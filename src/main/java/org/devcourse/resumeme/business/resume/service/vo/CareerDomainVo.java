package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class CareerDomainVo implements ComponentDomain {

    private Career career;

    public CareerDomainVo(Career career) {
        this.career = career;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return career.toComponent(resumeId);
    }

}
