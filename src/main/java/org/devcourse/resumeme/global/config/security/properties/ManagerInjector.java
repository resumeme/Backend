package org.devcourse.resumeme.global.config.security.properties;

import jakarta.annotation.PostConstruct;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.user.repository.UserRepository;
import org.devcourse.resumeme.global.auth.service.authorization.AuthorizationResolver;
import org.devcourse.resumeme.global.auth.service.authorization.OnlyOwn;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.devcourse.resumeme.global.auth.service.authorization.AuthorizationTarget.EVENTS;
import static org.devcourse.resumeme.global.auth.service.authorization.AuthorizationTarget.MENTEES;
import static org.devcourse.resumeme.global.auth.service.authorization.AuthorizationTarget.MENTORS;
import static org.devcourse.resumeme.global.auth.service.authorization.AuthorizationTarget.RESUMES;
import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTEE;
import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTOR;

@Component
public class ManagerInjector {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public ManagerInjector(EventRepository eventRepository, ResumeRepository resumeRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void postConstruct() {
        AuthorizationResolver resolver = new AuthorizationResolver(resumeRepository, eventRepository, userRepository);
        OWN_MENTEE.authorizationManager = new OnlyOwn(List.of(RESUMES, MENTEES), "ROLE_MENTEE", resolver);
        OWN_MENTOR.authorizationManager = new OnlyOwn(List.of(EVENTS, MENTORS), "ROLE_MENTOR", resolver);
    }

}
