package org.devcourse.resumeme.business.event.domain.vo;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;

public class EventReject implements EventUpdateModel {

    @Getter
    private Long menteeId;

    @Getter
    private String rejectMessage;

    public EventReject(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }


    @Override
    public void update(Event event) {
        event.reject(menteeId, rejectMessage);
    }

}
