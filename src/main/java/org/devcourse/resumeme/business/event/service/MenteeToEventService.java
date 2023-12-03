package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.domain.model.ApplimentUpdate;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.ApplyUpdateVo;
import org.devcourse.resumeme.global.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MenteeToEventService {

    private final MenteeToEventRepository menteeToEventRepository;

    private final EventRepository eventRepository;

    public Long getRecord(Long eventId, Long menteeId) {
        return menteeToEventRepository.findByMenteeId(menteeId).stream()
                .filter(record -> record.getEvent().getId().equals(eventId))
                .findFirst()
                .map(MenteeToEvent::getId)
                .orElse(null);
    }

    public Long update(Long eventId, ApplyUpdateVo applyUpdateVo) {
        Event event = eventRepository.findWithApplicantsById(eventId)
                .orElseThrow(() -> new EventException(EVENT_NOT_FOUND));

        ApplimentUpdate model = applyUpdateVo.toModel();

        return model.update(event);
    }

    public void acceptMentee(AcceptMenteeToEvent ids) {
        checkCanApply(ids.menteeId());
        Event event = eventRepository.findWithLockById(ids.eventId())
                .orElseThrow();

        event.acceptMentee(ids.menteeId(), ids.resumeId());
    }

    private void checkCanApply(Long menteeId) {
        Long menteeApplyCount = getApplyEventCount(menteeId);
        if (menteeApplyCount > 0) {
            throw new EventException(ExceptionCode.DUPLICATE_APPLICATION_EVENT);
        }
    }

    private Long getApplyEventCount(Long menteeId) {
        return menteeToEventRepository.findByMenteeId(menteeId).stream()
                .filter(MenteeToEvent::isAttending)
                .count();
    }

    public List<MenteeToEvent> getByMenteeId(Long menteeId) {
        return menteeToEventRepository.findByMenteeId(menteeId);
    }

}
