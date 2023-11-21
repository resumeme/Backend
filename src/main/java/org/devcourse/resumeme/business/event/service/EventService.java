package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.event.service.vo.EventReject;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.DUPLICATED_EVENT_OPEN;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Slf4j
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
    public Long getApplicantId(Long eventId, Long menteeId) {
        return getOne(eventId).getApplicantId(menteeId);
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

    @Transactional(readOnly = true)
    public List<Event> getAll(AllEventFilter filter) {
        if (filter.mentorId() != null) {
            return eventRepository.findAllByMentorId(filter.mentorId(), Pageable.unpaged()).getContent();
        }

        if (filter.menteeId() != null) {
            return eventRepository.findAllByApplicantsMenteeId(filter.menteeId(), Pageable.unpaged()).getContent();
        }

        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Event> getAllWithPage(AllEventFilter filter, Pageable pageable) {
        if (filter.mentorId() != null) {
            return eventRepository.findAllByMentorId(filter.mentorId(), pageable);
        }

        if (filter.menteeId() != null) {
            return eventRepository.findAllByApplicantsMenteeId(filter.menteeId(), pageable);
        }

        return eventRepository.findAll(pageable);
    }

    public void completeReview(Long eventId, String request, Long resumeId) {
        MenteeToEvent menteeToEvent = getMenteeToEvent(eventId, resumeId);

        menteeToEvent.completeEvent(request);
    }

    public MenteeToEvent getMenteeToEvent(Long eventId, Long resumeId) {
        Event event = getOne(eventId);
        MenteeToEvent menteeToEvent = event.getApplicants().stream()
                .filter(m -> m.isSameResume(resumeId))
                .findFirst()
                .orElseThrow(() -> new CustomException(RESUME_NOT_FOUND));

        return menteeToEvent;
    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void openBookedEvents() {
        log.info("scheduler 실행 됨");
        eventRepository.openBookedEvent(EventStatus.OPEN, LocalDateTime.now());
        eventRepository.closeApplyToEvent(EventStatus.CLOSE, LocalDateTime.now());
        eventRepository.finishEvent(EventStatus.FINISH, LocalDateTime.now());
    }

}
