package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public abstract class ComponentResponse {

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean reflectFeedback;

    protected ComponentResponse(ComponentInfo info) {
        this.componentId = info.getComponentId();
        this.originComponentId = info.getOriginComponentId();
        this.reflectFeedback = info.isReflectFeedback();
    }

}
