package org.devcourse.resumeme.global.config.security.properties;

import jakarta.annotation.PostConstruct;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.global.auth.service.authorization.AuthorizationResolver;
import org.devcourse.resumeme.global.auth.service.authorization.OnlyOwn;
import org.springframework.stereotype.Component;

import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTEE;
import static org.devcourse.resumeme.global.config.security.properties.Manager.OWN_MENTOR;

@Component
public class ManagerInjector {

    private final ResumeRepository resumeRepository;

    private final EventRepository eventRepository;

    public ManagerInjector(EventRepository eventRepository, ResumeRepository resumeRepository) {
        this.eventRepository = eventRepository;
        this.resumeRepository = resumeRepository;
    }

    @PostConstruct
    public void postConstruct() {
        AuthorizationResolver resolver = new AuthorizationResolver(resumeRepository, eventRepository);
        OWN_MENTEE.authorizationManager = new OnlyOwn("resumes", "ROLE_MENTEE", resolver);
        OWN_MENTOR.authorizationManager = new OnlyOwn("events", "ROLE_MENTOR", resolver);
    }

}
