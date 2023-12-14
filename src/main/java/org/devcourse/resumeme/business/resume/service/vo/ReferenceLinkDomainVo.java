package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.link.ReferenceLink;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class ReferenceLinkDomainVo implements ComponentDomain {

    private ReferenceLink referenceLink;

    public ReferenceLinkDomainVo(ReferenceLink referenceLink) {
        this.referenceLink = referenceLink;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return referenceLink.toComponent(resumeId);
    }

}
