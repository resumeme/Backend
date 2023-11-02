package org.devcourse.resumeme.global.auth.token;

import org.devcourse.resumeme.global.auth.model.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.devcourse.resumeme.domain.user.Role.ROLE_MENTEE;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class JwtServiceTest {

    @InjectMocks
    JwtService jwtService;

    private String accessToken;

    private String refreshToken;

    @BeforeEach
    void setUp() {
        Claims claims = new Claims(1L, ROLE_MENTEE.name(), new Date());
        accessToken = jwtService.createAccessToken(claims);
    }

    @Test
    void 액세스_토큰_검증에_성공한다() {
        jwtService.validate(accessToken);
    }

    @Test
    void 리프레시_토큰_생성에_성공한다() {
        jwtService.createRefreshToken();
    }

    @Test
    void 액세스_토큰을_추출하는데_성공한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access", accessToken);
        String extractedAccessToken = jwtService.extractAccessToken(request).get();
        jwtService.compareTokens(accessToken, extractedAccessToken);
    }

    @Test
    void 리프레시_토큰을_추출하는데_성공한다() {
        refreshToken = jwtService.createRefreshToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("refresh", refreshToken);
        String extractedRefreshToken = jwtService.extractRefreshToken(request).get();
        jwtService.compareTokens(refreshToken, extractedRefreshToken);
    }

}
