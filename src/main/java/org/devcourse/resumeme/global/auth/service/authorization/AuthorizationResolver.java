package org.devcourse.resumeme.global.auth.service.authorization;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.user.repository.mentee.MenteeRepository;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class AuthorizationResolver {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    public boolean resolve(Long userId, Long pathVariable, String target) {
        return switch (target) {
            case "events" -> eventRepository.findAllByMentorId(userId, Pageable.unpaged()).getContent().stream()
                    .anyMatch(event -> event.getId().equals(pathVariable));
            case "resumes" -> resumeRepository.findAllByMenteeId(userId).stream()
                    .anyMatch(resume -> resume.getId().equals(pathVariable));
            case "mentees" -> menteeRepository.findById(pathVariable).isPresent();
            case "mentors" -> mentorRepository.findById(pathVariable).isPresent();
            default -> throw new CustomException(BAD_REQUEST);
        };
    }

}
