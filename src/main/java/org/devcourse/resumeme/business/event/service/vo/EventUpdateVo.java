package org.devcourse.resumeme.business.event.service.vo;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;

public abstract class EventUpdateVo {

    @Getter
    protected final Long eventId;

    public EventUpdateVo(Long eventId) {
        this.eventId = eventId;
    }

    public abstract void update(Event event);

}
