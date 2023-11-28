package org.devcourse.resumeme.business.resume.controller.career.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Converter;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public abstract class ComponentResponse {

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean reflectFeedback;

    protected ComponentResponse(Converter converter) {
        this.componentId = converter.getComponentId();
        this.originComponentId = converter.getOriginComponentId();
        this.reflectFeedback = converter.isReflectFeedback();
    }

}
