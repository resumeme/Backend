package org.devcourse.resumeme.business.event.service.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static org.devcourse.resumeme.business.event.service.listener.EventCreation.*;

@Component
@RequiredArgsConstructor
public class EventCreationPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEventCreation(EventNoticeInfo eventCreationInfo) {
        EventCreation eventCreation = new EventCreation(this, eventCreationInfo);
        applicationEventPublisher.publishEvent(eventCreation);
    }

}
