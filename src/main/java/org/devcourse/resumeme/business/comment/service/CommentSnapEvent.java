package org.devcourse.resumeme.business.comment.service;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CommentSnapEvent extends ApplicationEvent {

    @Getter
    private final Long resumeId;

    @Getter
    private final Long eventId;

    public CommentSnapEvent(Object source, Long resumeId, Long eventId) {
        super(source);
        this.resumeId = resumeId;
        this.eventId = eventId;
    }

}
