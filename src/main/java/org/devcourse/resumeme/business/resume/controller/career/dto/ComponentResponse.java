package org.devcourse.resumeme.business.resume.controller.career.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class ComponentResponse {

    @Getter
    protected Long id;

    public ComponentResponse(Long id) {
        this.id = id;
    }

}
