package org.devcourse.resumeme.business.resume.controller.career.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDateTime;

@NoArgsConstructor
public abstract class ComponentResponse {

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean isReflectFeedback;

    @Getter
    private LocalDateTime createdDate;

    protected ComponentResponse(Component component) {
        this.componentId = component.getId();
        this.originComponentId = component.getOriginComponentId();
        this.isReflectFeedback = component.isReflectFeedBack();
        this.createdDate = component.getCreatedDate();
    }

}
