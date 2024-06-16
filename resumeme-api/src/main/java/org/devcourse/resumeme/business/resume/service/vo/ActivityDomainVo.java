package org.devcourse.resumeme.business.resume.service.vo;

import org.devcourse.resumeme.business.resume.domain.activity.Activity;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public class ActivityDomainVo implements ComponentDomain {

    private Activity activity;

    public ActivityDomainVo(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Component toComponent(Long resumeId) {
        return activity.toComponent(resumeId);
    }

}
