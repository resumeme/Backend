package org.devcourse.resumeme.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.global.auth.OAuth2CustomUserService;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2FailureHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2SuccessHandler;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthTokenProvider;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthTokenResolver;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

public class SecurityServiceConfig {

    @Bean
    public OAuthTokenResponseFilter oAuthTokenResponseFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                                             OAuth2SuccessHandler successHandler, OAuth2FailureHandler failureHandler ) {
        OAuthTokenResponseFilter oAuthTokenResponseFilter = new OAuthTokenResponseFilter(authenticationManager, objectMapper);
        oAuthTokenResponseFilter.setAuthenticationSuccessHandler(successHandler);
        oAuthTokenResponseFilter.setAuthenticationFailureHandler(failureHandler);

        return oAuthTokenResponseFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration, OAuthTokenProvider oAuthTokenProvider) throws Exception {
        ProviderManager providerManager = (ProviderManager)configuration.getAuthenticationManager();
        providerManager.getProviders().add(oAuthTokenProvider);

        return providerManager;
    }

    @Bean
    public OAuthTokenProvider oAuthTokenProvider(OAuth2CustomUserService service, ClientRegistrationRepository repository) {
        return new OAuthTokenProvider(service, repository, new OAuthTokenResolver());
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtService jwtService, MenteeService menteeService, MentorService mentorService) {
        return new JwtAuthorizationFilter(jwtService, mentorService, menteeService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://resumeme.vercel.app", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
