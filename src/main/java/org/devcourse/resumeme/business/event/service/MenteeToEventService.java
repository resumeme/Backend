package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.domain.model.ApplimentUpdate;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.devcourse.resumeme.business.event.service.vo.ApplyUpdateVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MenteeToEventService implements ApplyProvider {

    private final MenteeToEventRepository menteeToEventRepository;

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<MenteeToEvent> getEventsRelatedToResume(Long resumeId) {
        return menteeToEventRepository.findAllByResumeId(resumeId);
    }

    @Override
    public Long getApplyEventCount(Long menteeId) {
        return menteeToEventRepository.findByMenteeId(menteeId).stream()
                .filter(MenteeToEvent::isAttending)
                .count();
    }

    public void update(Long eventId, ApplyUpdateVo applyUpdateVo) {
        Event event = eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));

        ApplimentUpdate model = applyUpdateVo.toModel();
        model.update(event);
    }

}
