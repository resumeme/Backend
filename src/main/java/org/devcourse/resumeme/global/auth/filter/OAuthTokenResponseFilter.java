package org.devcourse.resumeme.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class OAuthTokenResponseFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_URL = "/login/oauth2/code";

    private final ObjectMapper objectMapper;

    public OAuthTokenResponseFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(LOGIN_URL, authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        CodeRequest codeRequest = objectMapper.readValue(request.getInputStream(), CodeRequest.class);

        return this.getAuthenticationManager().authenticate(new OAuthAuthenticationToken(codeRequest.code, codeRequest.loginProvider));
    }

    record CodeRequest(String loginProvider, String code) {

    }

}
