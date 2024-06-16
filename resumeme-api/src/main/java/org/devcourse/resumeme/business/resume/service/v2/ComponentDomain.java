package org.devcourse.resumeme.business.resume.service.v2;

import org.devcourse.resumeme.business.resume.entity.Component;

public interface ComponentDomain {

    Component toComponent(Long resumeId);

}
