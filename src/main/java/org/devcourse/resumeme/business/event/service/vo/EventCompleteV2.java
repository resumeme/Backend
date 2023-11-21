package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.vo.EventComplete;
import org.devcourse.resumeme.business.event.domain.vo.EventUpdateModel;

public class EventCompleteV2 implements EventUpdateVo {

    private Long resumeId;

    private String completeMessage;

    public EventCompleteV2(Long resumeId, String completeMessage) {
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    @Override
    public EventUpdateModel toModel() {
        return new EventComplete(resumeId, completeMessage);
    }

}
