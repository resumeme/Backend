package org.devcourse.resumeme.global.config;

import org.devcourse.resumeme.global.config.authorization.OnlyOwn;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

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
        OWN_MENTEE(new OnlyOwn("resumes", "ROLE_MENTEE")),
        OWN_MENTOR(new OnlyOwn("events", "ROLE_MENTOR"));

        final AuthorizationManager<RequestAuthorizationContext> manager;

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

    }

}
