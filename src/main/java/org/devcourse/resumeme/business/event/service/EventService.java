package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
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

    private final UserService userService;

    public Long create(Event event) {
        eventRepository.findAllByMentorId(event.getMentorId())
                .forEach(Event::checkOpen);
        Event savedEvent = eventRepository.save(event);
        User user = userService.getOne(savedEvent.getMentorId());
        Mentor mentor = Mentor.of(user);
        EventNoticeInfo eventNoticeInfo = new EventNoticeInfo(savedEvent, mentor);
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
            return eventRepository.findAllByMentorIdOrderByCreatedDateDesc(filter.mentorId(), pageable);
        }

        if (filter.menteeId() != null) {
            return eventRepository.findAllByApplicantsMenteeIdOrderByCreatedDateDesc(filter.menteeId(), pageable);
        }

        return eventRepository.findAllByOrderByCreatedDateDesc(pageable);
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
