package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.vo.EventReject;
import org.devcourse.resumeme.business.event.domain.vo.EventUpdateModel;

public final class EventRejectV2 implements EventUpdateVo {

    private final Long menteeId;

    private final String rejectMessage;

    public EventRejectV2(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }

    @Override
    public EventUpdateModel toModel() {
        return new EventReject(menteeId, rejectMessage);
    }

}
