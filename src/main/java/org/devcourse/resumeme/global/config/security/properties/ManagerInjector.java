package org.devcourse.resumeme.global.config.security.properties;

import jakarta.annotation.PostConstruct;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.user.repository.mentee.MenteeRepository;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.devcourse.resumeme.global.auth.service.authorization.AuthorizationResolver;
import org.devcourse.resumeme.global.auth.service.authorization.OnlyOwn;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTEE;
import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTOR;

@Component
public class ManagerInjector {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    public ManagerInjector(EventRepository eventRepository, ResumeRepository resumeRepository, MenteeRepository menteeRepository, MentorRepository mentorRepository) {
        this.eventRepository = eventRepository;
        this.resumeRepository = resumeRepository;
        this.menteeRepository = menteeRepository;
        this.mentorRepository = mentorRepository;
    }

    @PostConstruct
    public void postConstruct() {
        AuthorizationResolver resolver = new AuthorizationResolver(resumeRepository, eventRepository, menteeRepository, mentorRepository);
        OWN_MENTEE.authorizationManager = new OnlyOwn(List.of("resumes", "mentees"), "ROLE_MENTEE", resolver);
        OWN_MENTOR.authorizationManager = new OnlyOwn(List.of("events", "mentors"), "ROLE_MENTOR", resolver);
    }

}