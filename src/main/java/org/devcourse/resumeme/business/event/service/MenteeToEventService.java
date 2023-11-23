package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenteeToEventService implements ApplyProvider {

    private final MenteeToEventRepository menteeToEventRepository;

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

}
