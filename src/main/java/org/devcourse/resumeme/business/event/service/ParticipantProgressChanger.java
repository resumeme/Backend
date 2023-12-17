package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.repository.vo.MenteeToEventCondition;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantProgressChanger implements ParticipantProvider {

    private final MenteeToEventRepository menteeToEventRepository;

    @Override
    public void finishProgress(Long menteeId, Long resumeId) {
        MenteeToEventCondition condition = new MenteeToEventCondition(null, menteeId);
        menteeToEventRepository.findAllBy(condition.getExpression()).stream()
                .filter(participant -> participant.isSameResume(resumeId))
                .findFirst()
                .ifPresent(MenteeToEvent::editComplete);
    }
}
