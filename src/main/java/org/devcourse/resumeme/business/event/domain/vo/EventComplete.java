package org.devcourse.resumeme.business.event.domain.vo;

import org.devcourse.resumeme.business.event.domain.Event;

public class EventComplete implements EventUpdateModel {

    private Long resumeId;

    private String completeMessage;

    public EventComplete(Long resumeId, String completeMessage) {
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    @Override
    public void update(Event event) {
        event.complete(resumeId, completeMessage);
    }

}
