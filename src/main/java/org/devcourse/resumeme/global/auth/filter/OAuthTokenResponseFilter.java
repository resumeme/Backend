package org.devcourse.resumeme.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devcourse.resumeme.global.auth.model.login.OAuth2CustomUser;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;

public class OAuthTokenResponseFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_URL = "/api/v1/login/oauth2/code";

    private final ObjectMapper objectMapper;

    public OAuthTokenResponseFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(LOGIN_URL, authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, POST.name()));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        CodeRequest codeRequest = objectMapper.readValue(request.getInputStream(), CodeRequest.class);

        Authentication authenticate = this.getAuthenticationManager().authenticate(new OAuthAuthenticationToken(codeRequest.code, codeRequest.loginProvider));
        checkSignedUser(authenticate);

        return authenticate;
    }

    private void checkSignedUser(Authentication authenticate) {
        OAuth2CustomUser principal = (OAuth2CustomUser) authenticate.getPrincipal();
        if (principal.isNewUser()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("NOT_REGISTERED"), principal.getAuthenticationKey());
        }
    }

    record CodeRequest(String loginProvider, String code) {

    }

}
