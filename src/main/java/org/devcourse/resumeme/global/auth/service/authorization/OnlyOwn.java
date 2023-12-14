package org.devcourse.resumeme.global.auth.service.authorization;

import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class OnlyOwn implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<AuthorizationTarget> targets;

    private final String role;

    private final AuthorizationResolver resolver;

    public OnlyOwn(List<AuthorizationTarget> targets, String role, AuthorizationResolver resolver) {
        this.targets = targets;
        this.role = role;
        this.resolver = resolver;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        System.out.println("hello");
        if (!checkRole(authentication)) {
            log.debug("역할이 일치 하지 않음, role : {}", authentication.get().getAuthorities().stream().map(GrantedAuthority::getAuthority).toString());

            return new AuthorizationDecision(false);
        }

        Long userId = ((JwtUser) authentication.get().getPrincipal()).id();
        String uri = context.getRequest().getRequestURI();

        for (AuthorizationTarget target : targets) {
            String targetName = target.name().toLowerCase();
            String[] uris = uri.split(targetName);

            if (uris.length == 2) {
                String targetId = uris[1].split("/")[1];

                return new AuthorizationDecision(resolver.resolve(userId, Long.valueOf(targetId), target));
            }
        }
        log.debug("해당 사용자의 개인 리소스가 아님");

        return new AuthorizationDecision(false);
    }

    private boolean checkRole(Supplier<Authentication> authentication) {
        return authentication.get().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
                .contains(role);
    }

}
