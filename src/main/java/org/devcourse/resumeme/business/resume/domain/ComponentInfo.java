package org.devcourse.resumeme.business.resume.domain;

import lombok.Getter;
import org.devcourse.resumeme.business.resume.entity.Component;

public class ComponentInfo {

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean reflectFeedback;

    public ComponentInfo(Component component) {
        this.componentId = component.getId();
        this.originComponentId = component.getOriginComponentId();
        this.reflectFeedback = component.isReflectFeedBack();
    }

}
