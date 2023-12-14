package org.devcourse.resumeme.global.auth.service.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class DelegatingAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<AuthorizationManager<RequestAuthorizationContext>> managers;

    public DelegatingAuthorizationManager(List<AuthorizationManager<RequestAuthorizationContext>> managers) {
        this.managers = managers;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        String uri = object.getRequest().getRequestURI();
        for (AuthorizationManager<RequestAuthorizationContext> manager : managers) {
            if (manager.check(authentication, object).isGranted()) {
                return new AuthorizationDecision(true);
            }
        }

        log.info("cannot Access to entry point to {}", uri);

        return new AuthorizationDecision(false);
    }

}
