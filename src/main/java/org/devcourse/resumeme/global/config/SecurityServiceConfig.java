package org.devcourse.resumeme.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.global.auth.OAuth2CustomUserService;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2FailureHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2SuccessHandler;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

public class SecurityServiceConfig {

    @Bean
    public OAuthTokenResponseFilter oAuthTokenResponseFilter(OAuth2CustomUserService service, AuthenticationManager authenticationManager, ClientRegistrationRepository clientRegistrationRepository,
                                                             OAuth2SuccessHandler successHandler, OAuth2FailureHandler failureHandler, ObjectMapper objectMapper) {
        OAuthTokenResponseFilter oAuthTokenResponseFilter = new OAuthTokenResponseFilter(authenticationManager, service, clientRegistrationRepository, objectMapper);
        oAuthTokenResponseFilter.setAuthenticationSuccessHandler(successHandler);
        oAuthTokenResponseFilter.setAuthenticationFailureHandler(failureHandler);

        return oAuthTokenResponseFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtService jwtService, MenteeService menteeService, MentorService mentorService) {
        return new JwtAuthorizationFilter(jwtService, mentorService, menteeService);
    }

}
