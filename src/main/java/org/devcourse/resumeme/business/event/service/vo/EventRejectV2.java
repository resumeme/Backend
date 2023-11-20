package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.vo.EventUpdateModel;

public final class EventRejectV2 extends EventUpdateVo {

    private final Long menteeId;

    private final String rejectMessage;

    public EventRejectV2(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }

    @Override
    public EventUpdateModel toModel() {
        return null;
    }

}
