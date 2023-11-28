package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

public abstract class ComponentCreateRequest {

    public abstract ComponentDomain toVo();

}
