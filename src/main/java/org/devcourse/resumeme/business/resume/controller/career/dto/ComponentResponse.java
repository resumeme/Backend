package org.devcourse.resumeme.business.resume.controller.career.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public abstract class ComponentResponse {

    @Getter
    protected Long id;

    @Getter
    private LocalDateTime createdDate;

    public ComponentResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

}
