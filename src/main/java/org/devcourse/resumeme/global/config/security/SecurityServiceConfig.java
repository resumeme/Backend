package org.devcourse.resumeme.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.global.auth.filter.ExceptionHandlerFilter;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.RoleConsistencyCheckFilter;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2FailureHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2SuccessHandler;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthTokenProvider;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthTokenResolver;
import org.devcourse.resumeme.global.auth.model.jwt.JwtProperties;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.auth.service.login.OAuth2CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

public class SecurityServiceConfig {
    @Bean
    public HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler() {
            return new HttpStatusReturningLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public OAuthTokenResponseFilter oAuthTokenResponseFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                                             OAuth2SuccessHandler successHandler, OAuth2FailureHandler failureHandler ) {
        OAuthTokenResponseFilter oAuthTokenResponseFilter = new OAuthTokenResponseFilter(authenticationManager, objectMapper);
        oAuthTokenResponseFilter.setAuthenticationSuccessHandler(successHandler);
        oAuthTokenResponseFilter.setAuthenticationFailureHandler(failureHandler);

        return oAuthTokenResponseFilter;
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter(ObjectMapper objectMapper) {
        return new ExceptionHandlerFilter(objectMapper);
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
    public JwtService jwtService(JwtProperties jwtProperties) {
        return new JwtService(jwtProperties);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtService jwtService, UserService userService) {
        return new JwtAuthorizationFilter(jwtService, userService);
    }

    @Bean
    public RoleConsistencyCheckFilter roleConsistencyCheckFilter(UserService userService) {
        return new RoleConsistencyCheckFilter(userService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(/*"https://resumeme.vercel.app", "http://localhost:5173"*/"*"));
        configuration.setAllowedMethods(List.of("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.setExposedHeaders(List.of("cacheKey", "Authorization", "Refresh-Token"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
