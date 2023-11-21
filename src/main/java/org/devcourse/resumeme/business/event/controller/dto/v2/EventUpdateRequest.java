package org.devcourse.resumeme.business.event.controller.dto.v2;

import org.devcourse.resumeme.business.event.service.vo.EventCompleteV2;
import org.devcourse.resumeme.business.event.service.vo.EventRejectV2;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;

public class EventUpdateRequest {

    private Long menteeId;

    private String rejectMessage;

    private Long resumeId;

    private String completeMessage;

    public EventUpdateRequest(Long menteeId, String rejectMessage, Long resumeId, String completeMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    public EventUpdateVo toVo() {
        if (rejectMessage != null) {
            return toReject();
        }

        return toComplete();
    }

    private EventUpdateVo toReject() {
        return new EventRejectV2(menteeId, rejectMessage);
    }

    private EventUpdateVo toComplete() {
        return new EventCompleteV2(resumeId, completeMessage);
    }

}
