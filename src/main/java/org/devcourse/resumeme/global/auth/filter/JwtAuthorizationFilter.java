package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.domain.Role;
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
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_PENDING;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if (accessToken.isPresent()) {
            accessToken.filter(jwtService::isNotExpired)
                    .ifPresentOrElse(this::saveAuthentication, () -> checkRefreshToken(accessToken.get(), request, response));
        }
        filterChain.doFilter(request, response);
    }

    private void checkRefreshToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = jwtService.extractClaim(accessToken);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);

        if (refreshToken.isPresent()) {
            if (jwtService.isNotExpired(refreshToken.get()) && jwtService.compareTokens(findSavedTokenWithClaims(claims), refreshToken.get())) {
                log.info("리프레시 토큰 유효. new 액세스 토큰 발급 시작");
                Claims claimForNewToken = new Claims(claims.id(), claims.role(), new Date());
                String issuedAccessToken = jwtService.createAccessToken(claimForNewToken);
                saveAuthentication(issuedAccessToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                jwtService.setAccessTokenHeader(response, issuedAccessToken);
            }
        } else {
            throw new TokenException(INVALID_ACCESS_TOKEN);
        }
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
        if (isOutdatedClaim(claims)) {
            log.info("pending -> mentor : 새로 로그인 해야합니다.");
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(claims));
        log.info("Authentication 을 만들기 위한 Claims = {}", claims);
        log.info("context에 Authentication를 저장했습니다.");
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Claims claims) {
        return new UsernamePasswordAuthenticationToken(new JwtUser(claims.id()), null, List.of(claims::role));
    }

    private boolean isOutdatedClaim(Claims claims) {
        Long id = claims.id();
        Role role = Role.valueOf(claims.role());
        return role.equals(ROLE_PENDING) && mentorService.getOne(id).getRole().equals(ROLE_MENTOR);
    }

}
