package org.devcourse.resumeme.business.event.domain.model;

import org.devcourse.resumeme.business.event.domain.Event;

public class ApplimentComplete implements ApplimentUpdate {

    private Long resumeId;

    private String completeMessage;

    public ApplimentComplete(Long resumeId, String completeMessage) {
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    @Override
    public Long update(Event event) {
        event.complete(resumeId, completeMessage);

        return resumeId;
    }

}
