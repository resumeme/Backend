package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthorizationFilterTest extends ControllerUnitTest {

    @Test
    void 액세스토큰_인증_성공으로인한_성공_테스트() throws Exception {
        // given
        given(jwtService.extractAccessToken(any(HttpServletRequest.class))).willReturn(Optional.of("accessToken"));
        given(jwtService.isNotExpired("accessToken")).willReturn(true);
        given(jwtService.isNotManipulated("accessToken")).willReturn(true);
        given(jwtService.extractClaim("accessToken")).willReturn(new Claims(1L, "ROLE_MENTEE", new Date()));

        // when
        ResultActions result = mvc.perform(get("/filter-test"));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 리프레시토큰_인증_성공으로인한_성공_테스트() throws Exception {
        // given
        Mentee mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("refreshToken")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        given(jwtService.extractAccessToken(any(HttpServletRequest.class))).willReturn(Optional.of("accessToken"));
        given(jwtService.isNotManipulated("accessToken")).willReturn(false);
        given(jwtService.extractClaim(any())).willReturn(new Claims(1L, "ROLE_MENTEE", new Date()));
        given(jwtService.extractRefreshToken(any(HttpServletRequest.class))).willReturn(Optional.of("refreshToken"));
        given(jwtService.isNotManipulated("refreshToken")).willReturn(true);
        given(userService.getOne(1L)).willReturn(mentee.from());
        given(jwtService.compareTokens("refreshToken", "refreshToken")).willReturn(true);

        // when
        ResultActions result = mvc.perform(get("/filter-test"));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 리프레시토큰_유효성_검증_실패로인한_예외_발생() {
        // given
        given(jwtService.extractAccessToken(any(HttpServletRequest.class))).willReturn(Optional.of("accessToken"));
        given(jwtService.isNotManipulated("accessToken")).willReturn(false);
        given(jwtService.extractClaim(any())).willReturn(new Claims(1L, "ROLE_MENTEE", new Date()));
        given(jwtService.extractRefreshToken(any(HttpServletRequest.class))).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> mvc.perform(get("/filter-test")))
                .isInstanceOf(TokenException.class);
    }

}
