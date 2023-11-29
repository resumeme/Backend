package org.devcourse.resumeme.global.config.security.properties;

import org.devcourse.resumeme.global.auth.service.authorization.DelegatingAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.ArrayList;
import java.util.List;

public enum Manager {

    ALL((a, o) -> new AuthorizationDecision(true)),
    OWN_MENTEE(),
    OWN_MENTOR();

    AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    Manager() {
    }

    Manager(AuthorizationManager<RequestAuthorizationContext> authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    static AuthorizationManager<RequestAuthorizationContext> getManager(List<String> roles) {
        List<AuthorizationManager<RequestAuthorizationContext>> managers = new ArrayList<>();
        for (String role : roles) {
            try {
                managers.add(Manager.valueOf(role).authorizationManager);
            } catch (IllegalArgumentException e) {
                managers.add(AuthorityAuthorizationManager.hasRole(role));
            }
        }

        return new DelegatingAuthorizationManager(managers);
    }

}
