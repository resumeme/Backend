package org.devcourse.resumeme.global.auth.service.authorization;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.user.repository.mentee.MenteeRepository;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationResolver {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    public boolean resolve(Long userId, Long pathVariable, AuthorizationTarget target) {
        return switch (target) {
            case EVENTS -> eventRepository.findAllByMentorId(userId, Pageable.unpaged()).getContent().stream()
                    .anyMatch(event -> event.getId().equals(pathVariable));
            case RESUMES -> resumeRepository.findAllByMenteeId(userId).stream()
                    .anyMatch(resume -> resume.getId().equals(pathVariable));
            case MENTEES -> menteeRepository.findById(pathVariable).isPresent();
            case MENTORS -> mentorRepository.findById(pathVariable).isPresent();
        };
    }

}
