package org.devcourse.resumeme.global.config.security;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.global.auth.filter.ExceptionHandlerFilter;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.RoleConsistencyCheckFilter;
import org.devcourse.resumeme.global.auth.filter.handler.CustomLogoutHandler;
import org.devcourse.resumeme.global.config.security.properties.EndpointProperties;
import org.devcourse.resumeme.global.config.security.properties.Matcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Import(SecurityServiceConfig.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final EndpointProperties properties;

    private final RoleConsistencyCheckFilter roleConsistencyCheckFilter;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final OAuthTokenResponseFilter oAuthTokenResponseFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final CustomLogoutHandler customLogoutHandler;

    private final HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler;

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
                .headers(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .formLogin(c -> c.defaultSuccessUrl("/admin/applications"))
                .logout(logout -> logout.logoutUrl("/api/v1/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(logoutSuccessHandler)
                )
                .exceptionHandling(AbstractHttpConfigurer::disable);

        http.addFilterBefore(exceptionHandlerFilter, LogoutFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter, ExceptionHandlerFilter.class);
        http.addFilterAfter(roleConsistencyCheckFilter, JwtAuthorizationFilter.class);
        http.addFilterAfter(oAuthTokenResponseFilter, RoleConsistencyCheckFilter.class);

        return http.build();
    }

    private void setEndpoints(HttpSecurity http) throws Exception {
        for (Matcher matcher : properties.matchers()) {
            http.authorizeHttpRequests(registry ->
                    registry.requestMatchers(matcher.requestMatcher()).access(matcher.manager())
                            .requestMatchers(new IpAddressMatcher("127.0.0.1")).permitAll());
        }

    }

}
