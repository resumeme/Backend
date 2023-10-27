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
                    throw new EventException("DUPLICATED_EVENT_OPEN", "이미 오픈된 이벤트가 있습니다");
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
        Event event = eventRepository.findById(reject.eventId())
                .orElseThrow(() -> new EventException("NOT_FOUND", "이벤트를 찾을 수 없습니다"));

        event.reject(reject.menteeId(), reject.rejectMessage());
    }

}
