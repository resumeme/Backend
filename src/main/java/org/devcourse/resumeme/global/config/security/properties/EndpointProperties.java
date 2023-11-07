package org.devcourse.resumeme.global.config.security.properties;

import jakarta.annotation.PostConstruct;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.global.auth.service.authorization.AuthorizationResolver;
import org.devcourse.resumeme.global.auth.service.authorization.OnlyOwn;
import org.devcourse.resumeme.global.config.security.properties.EndpointProperties.Matcher.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ConfigurationProperties("endpoint")
public class EndpointProperties {

    private final List<Matcher> matchers;

    private final List<String> ignores;

    public EndpointProperties(List<PropertyMatcher> matchers, List<String> ignores) {
        this.matchers = getMatchers(matchers);
        this.ignores = ignores;
    }

    private static List<Matcher> getMatchers(List<PropertyMatcher> matchers) {
        Map<Request, List<String>> tempMatcher = new HashMap<>();

        for (PropertyMatcher matcher : matchers) {
            String role = matcher.role.toUpperCase();

            for (Map.Entry<String, List<String>> entry : matcher.matcher.entrySet()) {
                String method = entry.getKey().toUpperCase();

                for (String endpoint : entry.getValue()) {
                    Request endpoint1 = new Request(method, endpoint);
                    List<String> roles = tempMatcher.getOrDefault(endpoint1, new ArrayList<>());
                    roles.add(role);
                    tempMatcher.put(endpoint1, roles);
                }
            }
        }

        return tempMatcher.entrySet().stream()
                .map(entry -> new Matcher(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<Matcher> matchers() {
        return matchers;
    }

    public List<String> ignores() {
        return ignores;
    }

    public record PropertyMatcher(Map<String, List<String>> matcher, String role) {

    }

    public record Matcher(Request request, List<String> roles) {

        record Request(String method, String endpoint) {

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Request endpoint1 = (Request) o;
                return Objects.equals(method, endpoint1.method) && Objects.equals(endpoint, endpoint1.endpoint);
            }

            @Override
            public int hashCode() {
                return Objects.hash(method, endpoint);
            }

        }

        public RequestMatcher requestMatcher() {
            return new AntPathRequestMatcher(request.endpoint, request.method);
        }

        public AuthorizationManager<RequestAuthorizationContext> manager() {
            return Manager.getManager(roles);
        }

        enum Manager {

            ALL((a, o) -> new AuthorizationDecision(true)),
            OWN_MENTEE(),
            OWN_MENTOR();

            AuthorizationManager<RequestAuthorizationContext> authorizationManager;

            Manager() {
            }

            Manager(AuthorizationManager<RequestAuthorizationContext> authorizationManager) {
                this.authorizationManager = authorizationManager;
            }

            static AuthorizationManager<RequestAuthorizationContext> getManager(List<String> role) {
                if (role.size() == 1) {
                    String roleName = role.get(0).toUpperCase();

                    for (Manager value : values()) {
                        if (value.name().equals(roleName)) {
                            return value.authorizationManager;
                        }
                    }
                }

                return AuthorityAuthorizationManager.hasAnyRole(role.toArray(new String[]{}));
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
                    OWN_MENTEE.authorizationManager = new OnlyOwn("resumes", "ROLE_MENTEE", resolver);
                    OWN_MENTOR.authorizationManager = new OnlyOwn("events", "ROLE_MENTOR", resolver);
                }

            }
        }

    }

}
