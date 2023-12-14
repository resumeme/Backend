package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.service.UserService;
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

import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if (accessToken.isPresent()) {
            accessToken.filter(jwtService::isNotExpired)
                    .ifPresentOrElse(this::saveAuthentication, () -> checkRefreshToken(jwtService.extractClaim(accessToken.get()), request, response));
        }

        filterChain.doFilter(request, response);
    }

    private void checkRefreshToken(Claims claims, HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        if (refreshToken.isEmpty()) {
            throw new TokenException(INVALID_ACCESS_TOKEN);
        }

        if (isValidRefreshToken(claims.id(), refreshToken.get())) {
            log.info("리프레시 토큰 유효. new 액세스 토큰 발급 시작");
            Claims claimForNewToken = new Claims(claims.id(), claims.role(), new Date());
            String issuedAccessToken = jwtService.createAccessToken(claimForNewToken);
            saveAuthentication(issuedAccessToken);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            jwtService.setAccessTokenHeader(response, issuedAccessToken);
        }
    }

    private boolean isValidRefreshToken(Long userId, String refreshToken) {
        return jwtService.isNotExpired(refreshToken) && jwtService.compareTokens(findSavedTokenWithUserId(userId), refreshToken);
    }

    private String findSavedTokenWithUserId(Long userId) {
        return userService.getOne(userId).getRefreshToken();
    }

    private void saveAuthentication(String accessToken) {
        Claims claims = jwtService.extractClaim(accessToken);
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(claims));
        log.info("Authentication를 저장했습니다. Claims = {}", claims);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Claims claims) {
        return new UsernamePasswordAuthenticationToken(new JwtUser(claims.id()), null, List.of(claims::role));
    }

}
