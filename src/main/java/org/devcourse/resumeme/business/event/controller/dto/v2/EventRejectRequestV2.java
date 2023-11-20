package org.devcourse.resumeme.business.event.controller.dto.v2;

import org.devcourse.resumeme.business.event.service.vo.EventRejectV2;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;

public final class EventRejectRequestV2 extends EventUpdateRequest {

    private final Long menteeId;

    private final String rejectMessage;

    public EventRejectRequestV2(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }

    @Override
    public EventUpdateVo toVo() {
        return new EventRejectV2(menteeId, rejectMessage);
    }

}
