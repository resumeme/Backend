package org.devcourse.resumeme.global.config;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.global.auth.filter.ExceptionHandlerFilter;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.config.properties.EndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Import(SecurityServiceConfig.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final EndpointProperties properties;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final OAuthTokenResponseFilter oAuthTokenResponseFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(properties.ignores().toArray(new String[]{}));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        setEndpoints(http);
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(exceptionHandlerFilter, LogoutFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter, LogoutFilter.class);
        http.addFilterAfter(oAuthTokenResponseFilter, JwtAuthorizationFilter.class);

        return http.build();
    }

    private void setEndpoints(HttpSecurity http) throws Exception {
        for (EndpointProperties.Matcher matcher : properties.matchers()) {
            for (Map.Entry<String, List<String>> entry : matcher.matcher().entrySet()) {
                String method = entry.getKey().toUpperCase();

                for (String endPoint : entry.getValue()) {
                    http.authorizeHttpRequests(registry ->
                            registry.requestMatchers(new AntPathRequestMatcher(endPoint, method))
                                    .access(matcher.manager())
                    );
                }
            }
        }

    }

}
