package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.repository.EventRepository;
import org.springframework.stereotype.Service;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Long create(Event event) {
        return eventRepository.save(event).getId();
    }

    public int updateMaximumAttendeeCount(Long eventId, int newMaxCount) {
        Event updatedEvent = eventRepository.findWithLockById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND))
                .updateMaximumCount(newMaxCount);

        return eventRepository.save(updatedEvent).remainSeat();
    }

}
