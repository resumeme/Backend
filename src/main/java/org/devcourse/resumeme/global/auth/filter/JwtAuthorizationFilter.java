package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.exception.TokenException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::validate)
                .ifPresentOrElse(this::saveAuthentication, () -> checkRefreshToken(request, response));

        filterChain.doFilter(request, response);
    }

    private void checkRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        Claims claims;
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if (accessToken.isEmpty()) {
            return;
        }
        claims = jwtService.extractClaim(accessToken.get());

        jwtService.extractRefreshToken(request).ifPresentOrElse(token -> {
                    if (jwtService.validate(token) && jwtService.compareTokens(findSavedTokenWithClaims(claims), token)) {
                        String issuedAccessToken = jwtService.createAccessToken(new Claims(claims.id(), claims.role(), new Date()));
                        saveAuthentication(issuedAccessToken);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        jwtService.setAccessTokenHeader(response, issuedAccessToken);
                    }
                }, () -> {
                    throw new TokenException(INVALID_ACCESS_TOKEN);
                }
        );
    }

    private String findSavedTokenWithClaims(Claims claims) {
        Long id = claims.id();
        if (claims.role().equals(ROLE_MENTEE.name())) {
            return menteeService.getOneSimple(id).getRefreshToken();
        }

        return mentorService.getOneSimple(id).getRefreshToken();
    }

    private void saveAuthentication(String accessToken) {
        Claims claims = jwtService.extractClaim(accessToken);
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(claims));
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Claims claims) {
        return new UsernamePasswordAuthenticationToken(new JwtUser(claims.id()), null, List.of(claims::role));
    }

}
