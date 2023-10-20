package org.devcourse.resumeme.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final EndpointProperties properties;

    private final HandlerMappingIntrospector introspector;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        setEndpoints(http);
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private void setEndpoints(HttpSecurity http) throws Exception {
        for (String endpoint : properties.getPermitAll()) {
            http.authorizeHttpRequests(registry ->
                    registry.requestMatchers(new MvcRequestMatcher(introspector, endpoint)).permitAll()
            );
        }

        for (Map.Entry<String, List<String>> entry : properties.getRoles().entrySet()) {
            String roleName = entry.getKey();

            for (String endpoint : entry.getValue()) {
                http.authorizeHttpRequests(registry ->
                        registry.requestMatchers(new MvcRequestMatcher(introspector, endpoint)).hasRole(roleName.toUpperCase()));
            }
        }
    }

}
