package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ParticipantProgressChanger implements ParticipantProvider {

    private final MenteeToEventRepository menteeToEventRepository;

    @Override
    public void finishProgress(Long menteeId, Long resumeId) {
        List<MenteeToEvent> participants = menteeToEventRepository.findByMenteeId(menteeId);

        participants.stream()
                .filter(participant -> participant.isSameResume(resumeId))
                .findFirst()
                .ifPresent(MenteeToEvent::editComplete);
    }
}
