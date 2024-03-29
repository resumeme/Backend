package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.listener.EventCreationPublisher;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
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
        checkAlreadyOpenEvent(event);
        Event savedEvent = eventRepository.save(event);
        sendNotificationMail(savedEvent);

        return savedEvent.getId();
    }

    private void checkAlreadyOpenEvent(Event event) {
        eventRepository.findAllByMentorId(event.getMentorId())
                .forEach(Event::checkOpen);
    }

    private void sendNotificationMail(Event savedEvent) {
        UserResponse user = userProvider.getOne(savedEvent.getMentorId());

        EventNoticeInfo eventNoticeInfo = new EventNoticeInfo(savedEvent, user);
        eventCreationPublisher.publishEventCreation(eventNoticeInfo);
    }

    @Transactional(readOnly = true)
    public Event getOne(Long eventId) {
        return eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Event> getAll(Pageable pageable) {
        Page<Event> events = eventRepository.findAllByOrderByCreatedDateDesc(pageable);
        events.forEach(Event::getApplicantsCount);

        return events;
    }

    public void update(EventUpdateVo updateVo) {
        Event event = getOne(updateVo.getEventId());
        updateVo.update(event);
    }

    public void checkCommentAvailableDate(Long eventId) {
        Event event = getOne(eventId);
        event.checkDate();
    }

    public String getOverallReview(Event event, Long resumeId) {
        return event.getApplicants().stream()
                .filter(m -> m.isSameResume(resumeId))
                .findFirst()
                .orElseThrow(() -> new CustomException(RESUME_NOT_FOUND))
                .getOverallReview();
    }

}
