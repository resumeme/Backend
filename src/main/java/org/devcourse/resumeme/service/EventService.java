package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Long create(Event event) {
        return eventRepository.save(event).getId();
    }

}
