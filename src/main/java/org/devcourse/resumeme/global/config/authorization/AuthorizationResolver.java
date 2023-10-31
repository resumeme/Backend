package org.devcourse.resumeme.global.config.authorization;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.repository.EventRepository;
import org.devcourse.resumeme.repository.ResumeRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationResolver {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    public boolean resolve(Long userId, Long pathVariable, String target) {
        if (target.equals("events")) {
            return eventRepository.findAllByMentorId(userId).stream()
                    .anyMatch(event -> event.getId().equals(pathVariable));
        }

        return resumeRepository.findAllByMenteeId(userId).stream()
                .anyMatch(resume -> resume.getId().equals(pathVariable));
    }

}
