package org.devcourse.resumeme.global.config.security.properties;

import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public record Matcher(Request request, List<String> roles) {

    public RequestMatcher requestMatcher() {
        return new AntPathRequestMatcher(request.endpoint(), request.method());
    }

    public AuthorizationManager<RequestAuthorizationContext> manager() {
        return Manager.getManager(roles);
    }

}
