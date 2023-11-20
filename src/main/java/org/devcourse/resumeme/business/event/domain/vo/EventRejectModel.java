package org.devcourse.resumeme.business.event.domain.vo;

import lombok.Getter;

public class EventRejectModel extends EventUpdateModel {

    @Getter
    private final Long menteeId;

    @Getter
    private final String rejectMessage;

    public EventRejectModel(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }

}
