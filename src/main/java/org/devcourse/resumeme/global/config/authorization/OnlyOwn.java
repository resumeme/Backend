package org.devcourse.resumeme.global.config.authorization;

import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.auth.model.JwtUser;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
public class OnlyOwn implements AuthorizationManager<RequestAuthorizationContext> {

    private final String target;

    private final String role;

    private final AuthorizationResolver resolver;

    public OnlyOwn(String target, String role, AuthorizationResolver resolver) {
        this.target = target;
        this.role = role;
        this.resolver = resolver;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        if (checkRole(authentication)) {
            Long userId = ((JwtUser) authentication.get().getPrincipal()).id();
            String uri = context.getRequest().getRequestURI();
            String[] parameters = uri.split("/");

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].equals(target)) {
                    return new AuthorizationDecision(resolver.resolve(userId, Long.valueOf(parameters[i + 1]), target));
                }
            }
            log.debug("해당 사용자의 개인 리소스가 아님");
        }
        log.debug("역할이 일치 하지 않음, role : {}", authentication.get().getAuthorities().stream().map(GrantedAuthority::getAuthority).toString());

        return new AuthorizationDecision(false);
    }

    private boolean checkRole(Supplier<Authentication> authentication) {
        return authentication.get().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
                .contains(role);
    }

}
