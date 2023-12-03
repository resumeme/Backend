package org.devcourse.resumeme.global.auth.service.authorization;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationResolver {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public boolean resolve(Long userId, Long pathVariable, AuthorizationTarget target) {
        return switch (target) {
            case EVENTS -> eventRepository.findAllByMentorIdOrderByCreatedDateDesc(userId, Pageable.unpaged()).getContent().stream()
                    .anyMatch(event -> event.getId().equals(pathVariable));
            case RESUMES -> resumeRepository.findAllByMenteeId(userId).stream()
                    .anyMatch(resume -> resume.getId().equals(pathVariable));
            case MENTEES, MENTORS -> userRepository.findById(pathVariable).isPresent();
        };
    }

}
