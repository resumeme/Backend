package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.listener.EventCreationPublisher;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.business.event.service.vo.EventsFoundCondition;
import org.devcourse.resumeme.business.user.service.UserProvider;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devcourse.resumeme.business.event.service.listener.EventCreation.EventNoticeInfo;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventCreationPublisher eventCreationPublisher;

    private final EventRepository eventRepository;

    private final UserProvider userProvider;

    public Long create(Event event) {
        eventRepository.findAllByMentorId(event.getMentorId())
                .forEach(Event::checkOpen);
        Event savedEvent = eventRepository.save(event);
        UserResponse user = userProvider.getOne(savedEvent.getMentorId());
        EventNoticeInfo eventNoticeInfo = new EventNoticeInfo(savedEvent, user);
        eventCreationPublisher.publishEventCreation(eventNoticeInfo);

        return savedEvent.getId();
    }

    @Transactional(readOnly = true)
    public Event getOne(Long eventId) {
        return eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Event> getAllWithPage(EventsFoundCondition condition, Pageable pageable) {
        return switch (condition.role()) {
            case MENTEE -> eventRepository.findAllByApplicantsMenteeIdOrderByCreatedDateDesc(condition.userId(), pageable);
            case MENTOR -> eventRepository.findAllByMentorIdOrderByCreatedDateDesc(condition.userId(), pageable);
            case ALL -> eventRepository.findAllByOrderByCreatedDateDesc(pageable);
        };
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

}
