package org.devcourse.resumeme.business.event;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;
import org.springframework.context.ApplicationEvent;

public class EventCreation extends ApplicationEvent {

    @Getter
    private final Event event;

    public EventCreation(Object source, Event event) {
        super(source);
        this.event = event;
    }

}
