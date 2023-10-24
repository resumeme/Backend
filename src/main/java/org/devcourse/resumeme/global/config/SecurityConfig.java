package org.devcourse.resumeme.global.config;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.global.auth.OAuth2CustomUserService;
import org.devcourse.resumeme.global.auth.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final EndpointProperties properties;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final OAuth2CustomUserService oAuth2CustomUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        setEndpoints(http);
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(configure ->
                        configure.userInfoEndpoint(userConfig ->
                                        userConfig.userService(oAuth2CustomUserService))
                                .successHandler(oAuth2SuccessHandler));

        return http.build();
    }

    private void setEndpoints(HttpSecurity http) throws Exception {
        for (Map.Entry<String, List<String>> entry : properties.getPermitAll().entrySet()) {
            String method = entry.getKey().toUpperCase();

            for (String endPoint : entry.getValue()) {
                http.authorizeHttpRequests(registry ->
                        registry.requestMatchers(new AntPathRequestMatcher(endPoint, method)).permitAll());
            }
        }

        for (EndpointProperties.Matcher role : properties.getRoles()) {
            String[] roles = role.getRole().stream()
                    .map(String::toUpperCase)
                    .toArray(String[]::new);

            for (Map.Entry<String, List<String>> entry : role.getMatcher().entrySet()) {
                String method = entry.getKey().toUpperCase();

                for (String endPoint : entry.getValue()) {
                    http.authorizeHttpRequests(registry ->
                            registry.requestMatchers(new AntPathRequestMatcher(endPoint, method)).hasAnyRole(roles));
                }
            }
        }
    }

}
