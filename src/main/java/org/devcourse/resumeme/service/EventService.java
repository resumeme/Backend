package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.repository.EventRepository;
import org.devcourse.resumeme.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.service.vo.EventReject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.DUPLICATED_EVENT_OPEN;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.EVENT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Long create(Event event) {
        List<Event> eventsWithMentor = eventRepository.findAllByMentor(event.getMentor());
        eventsWithMentor.stream()
                .filter(Event::isOpen)
                .forEach(eventWithMentor -> {
                    throw new EventException(DUPLICATED_EVENT_OPEN);
                });

        return eventRepository.save(event).getId();
    }

    public Event acceptMentee(AcceptMenteeToEvent ids) {
        Event event = eventRepository.findWithLockById(ids.eventId())
                .orElseThrow();
        event.acceptMentee(ids.menteeId(), ids.resumeId());

        return event;
    }

    @Transactional(readOnly = true)
    public Long getApplicantId(Event event, Long menteeId) {
        return event.getApplicantId(menteeId);
    }

    public void reject(EventReject reject) {
        Event event = getOne(reject.eventId());

        event.reject(reject.menteeId(), reject.rejectMessage());
    }

    @Transactional(readOnly = true)
    public Event getOne(Long eventId) {
        return eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
    }

    public void requestReview(Long eventId, Long menteeId) {
        getOne(eventId).requestReview(menteeId);
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

}
