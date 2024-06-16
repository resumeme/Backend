package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.Event;

public class EventReopen extends EventUpdateVo {

    public EventReopen(Long eventId) {
        super(eventId);
    }

    @Override
    public void update(Event event) {
        event.reOpenEvent();
    }

}
