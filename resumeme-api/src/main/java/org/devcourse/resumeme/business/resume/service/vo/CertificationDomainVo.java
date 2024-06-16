package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class CertificationDomainVo implements ComponentDomain {

    private Certification certification;

    public CertificationDomainVo(Certification certification) {
        this.certification = certification;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return certification.toComponent(resumeId);
    }

}
