package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.devcourse.resumeme.business.event.service.EventCreation.*;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventCreationPublisher eventCreationPublisher;

    private final EventRepository eventRepository;

    public Long create(Event event) {
        eventRepository.findAllByMentor(event.getMentor())
                .forEach(Event::checkOpen);
        Event savedEvent = eventRepository.save(event);
        EventNoticeInfo eventNoticeInfo = new EventNoticeInfo(savedEvent);
        eventCreationPublisher.publishEventCreation(eventNoticeInfo);

        return savedEvent.getId();
    }

    @Transactional(readOnly = true)
    public Event getOne(Long eventId) {
        return eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
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

    public String getOverallReview(Event event, Long resumeId) {
        return event.getApplicants().stream()
                .filter(m -> m.isSameResume(resumeId))
                .findFirst()
                .orElseThrow(() -> new CustomException(RESUME_NOT_FOUND))
                .getOverallReview();
    }

    public void update(EventUpdateVo updateVo) {
        Event event = getOne(updateVo.getEventId());
        updateVo.update(event);
    }

    public void checkCommentAvailableDate(Long eventId) {
        Event event = getOne(eventId);
        event.checkDate();
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void openBookedEvents() {
        log.info("scheduler 실행 됨");
        eventRepository.openBookedEvent(EventStatus.OPEN, LocalDateTime.now());
        eventRepository.closeApplyToEvent(EventStatus.CLOSE, LocalDateTime.now());
        eventRepository.finishEvent(EventStatus.FINISH, LocalDateTime.now());
    }
}
