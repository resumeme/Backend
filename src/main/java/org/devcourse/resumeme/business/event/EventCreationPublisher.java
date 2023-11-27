package org.devcourse.resumeme.business.event;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventCreationPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEventCreation(Event event) {
        EventCreation eventCreation = new EventCreation(this, event);
        applicationEventPublisher.publishEvent(eventCreation);
    }

}
