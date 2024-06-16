package org.devcourse.resumeme.global.auth.service.jwt;

import org.assertj.core.api.Assertions;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.Optional;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.global.auth.model.jwt.JwtProperties.TokenInfo;

class JwtServiceTest {

    private final String ACCESS_TOKEN_NAME = "Authorization";

    private final String REFRESH_TOKEN_NAME = "Refresh-Token";

    private final JwtProperties jwtProperties = new JwtProperties("jwtTestKey", new TokenInfo(ACCESS_TOKEN_NAME, 1), new TokenInfo(REFRESH_TOKEN_NAME, 3));

    private final JwtService jwtService = new JwtService(jwtProperties);

    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        Claims claims = new Claims(1L, ROLE_MENTEE.name(), new Date());
        accessToken = jwtService.createAccessToken(claims);
    }

    @Test
    void 액세스_토큰_검증에_성공한다() {
        Assertions.assertThat(jwtService.isNotManipulated(accessToken)).isTrue();
        Assertions.assertThat(jwtService.isNotExpired(accessToken)).isTrue();
    }

    @Test
    void 만료된_액세스_토큰은_검증에_실패한다() throws InterruptedException {
        String accessTokenExpiresIn2sec = jwtService.createAccessToken(new Claims(2L, ROLE_MENTEE.name(), new Date()));
        Thread.sleep(2000);
        Assertions.assertThat(jwtService.isNotExpired(accessTokenExpiresIn2sec)).isFalse();
    }


    @Test
    void 리프레시_토큰_생성에_성공한다() {
        String refreshToken1 = jwtService.createRefreshToken();
    }

    @Test
    void 액세스_토큰을_추출하는데_성공한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ACCESS_TOKEN_NAME, accessToken);
        String extractedAccessToken = jwtService.extractAccessToken(request).get();

        jwtService.compareTokens(accessToken, extractedAccessToken);
    }

    @Test
    void 요청_헤더에_토큰이_없으면_액세스_토큰을_추출하는데_실패한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Optional<String> extractedAccessToken = jwtService.extractAccessToken(request);

        Assertions.assertThat(extractedAccessToken).isEmpty();
    }

    @Test
    void 리프레시_토큰을_추출하는데_성공한다() {
        refreshToken = jwtService.createRefreshToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(REFRESH_TOKEN_NAME, refreshToken);
        String extractedRefreshToken = jwtService.extractRefreshToken(request).get();
        jwtService.compareTokens(refreshToken, extractedRefreshToken);
    }

    @Test
    void 요청_헤더에_토큰이_없으면_리프레시_토큰을_추출하는데_실패한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Optional<String> extractedRefreshToken = jwtService.extractRefreshToken(request);

        Assertions.assertThat(extractedRefreshToken).isEmpty();
    }

    @Test
    void 액세스_토큰에서_클레임을_뽑아내는_데_성공한다() {
        Claims claims = new Claims(1L, ROLE_MENTOR.name(), new Date());
        String accessTokenIssued = jwtService.createAccessToken(claims);
        Claims extractedClaims = jwtService.extractClaim(accessTokenIssued);

        Assertions.assertThat(extractedClaims.id()).isEqualTo(claims.id());
        Assertions.assertThat(extractedClaims.role()).isEqualTo(claims.role());
    }


}
