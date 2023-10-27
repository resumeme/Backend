package org.devcourse.resumeme.global.auth.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.JwtUser;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.devcourse.resumeme.domain.user.Role.ROLE_MENTEE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthorizationFilter active");

        Optional<String> accessToken = jwtService.extractAccessToken(request);
        log.info("accessToken from request = {}", accessToken);

        try {
            accessToken.ifPresent(token -> {
                log.info("if present ==========");
                jwtService.validate(token);
                log.info("if present222 ==========");
                saveAuthentication(token);
            });

        } catch (TokenExpiredException expiredException) {
            jwtService.extractRefreshToken(request).ifPresent(refreshToken -> {
                try {
                    jwtService.validate(refreshToken);
                    Claims claims = jwtService.extractClaim(accessToken.get());

                    if (ROLE_MENTEE.toString().equals(claims.role())) {
                        if (jwtService.compareTokens(menteeService.getOne(claims.id()).getRefreshToken(), refreshToken)) {
                            Map<String, String> tokens = jwtService.createAndSendNewTokens(claims, response);
                            menteeService.updateRefreshToken(claims.id(), tokens.get("refresh"));
                        }
                    } else {
                        if (jwtService.compareTokens(mentorService.getOne(claims.id()).getRefreshToken(), refreshToken)) {
                            Map<String, String> tokens = jwtService.createAndSendNewTokens(claims, response);
                            mentorService.updateRefreshToken(claims.id(), tokens.get("refresh"));
                        }
                    }
                } catch (Exception e) {
                    log.debug("invalid access token");
                }
            });
        } catch (Exception e) {
            log.debug("invalid access token");
        }

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(String accessToken) {
        log.info("saveAuthentication ===========");
        Claims claims = jwtService.extractClaim(accessToken);
        System.out.println("claims = " + claims);
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(claims));

        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Claims claims) {
        System.out.println("claims.role() = " + claims.role());
        return new UsernamePasswordAuthenticationToken(new JwtUser(claims.id()), null, List.of(() -> "ROLE_MENTEE"));
    }

}
