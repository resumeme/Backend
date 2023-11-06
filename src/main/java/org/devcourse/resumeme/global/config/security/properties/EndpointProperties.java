package org.devcourse.resumeme.global.config.security.properties;

import jakarta.annotation.PostConstruct;
import org.devcourse.resumeme.global.auth.service.authorization.AuthorizationResolver;
import org.devcourse.resumeme.global.auth.service.authorization.OnlyOwn;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("endpoint")
public record EndpointProperties(List<Matcher> matchers, List<String> ignores) {

    public record Matcher(Map<String, List<String>> matcher, String role) {

        public AuthorizationManager<RequestAuthorizationContext> manager() {
            return Manager.getManager(role);
        }

    }

    enum Manager {

        ALL((a, o) -> new AuthorizationDecision(true)),
        OWN_MENTEE(),
        OWN_MENTOR();

        AuthorizationManager<RequestAuthorizationContext> manager;

        Manager() {
        }

        Manager(AuthorizationManager<RequestAuthorizationContext> manager) {
            this.manager = manager;
        }

        static AuthorizationManager<RequestAuthorizationContext> getManager(String role) {
            String roleName = role.toUpperCase();

            for (Manager value : values()) {
                if (value.name().equals(roleName)) {
                    return value.manager;
                }
            }

            return AuthorityAuthorizationManager.hasRole(roleName);
        }

        @Component
        public static class ManagerInjector {

            private final ResumeRepository resumeRepository;

            private final EventRepository eventRepository;

            public ManagerInjector(EventRepository eventRepository, ResumeRepository resumeRepository) {
                this.eventRepository = eventRepository;
                this.resumeRepository = resumeRepository;
            }

            @PostConstruct
            public void postConstruct() {
                AuthorizationResolver resolver = new AuthorizationResolver(resumeRepository, eventRepository);
                OWN_MENTEE.manager = new OnlyOwn("resumes", "ROLE_MENTEE", resolver);
                OWN_MENTOR.manager = new OnlyOwn("events", "ROLE_MENTOR", resolver);
            }

        }
    }

}
