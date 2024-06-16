package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class ForeignLanguageDomainVo implements ComponentDomain {

    private ForeignLanguage foreignLanguage;

    public ForeignLanguageDomainVo(ForeignLanguage foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return foreignLanguage.toComponent(resumeId);
    }

}
