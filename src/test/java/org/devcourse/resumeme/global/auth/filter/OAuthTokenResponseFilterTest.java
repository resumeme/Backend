package org.devcourse.resumeme.global.auth.filter;

import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.global.auth.filter.resolver.OAuthAuthenticationToken;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2CustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.PROVIDER;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuthTokenResponseFilterTest extends ControllerUnitTest {

    private final String ACCESS_TOKEN_NAME = "Authorization";

    private final String REFRESH_TOKEN_NAME = "Refresh-Token";

    @Test
    void 사용자는_로그인을_oauth를_통해_할수있다() throws Exception {
        // given
        String code = "code";
        String loginProvider = "kakao";

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(new OAuth2CustomUser(new UserCommonInfo(1L, "backdoonge@kakao.com", Role.ROLE_MENTEE), Map.of()), List.of(() -> "ROLE_MENTEE"), "kakao");
        given(providerManager.authenticate(new OAuthAuthenticationToken(code, loginProvider))).willReturn(token);

        // when
        ResultActions result = mvc.perform(post("/api/v1/login/oauth2/code")
                .content(mapper.writeValueAsBytes(new OAuthTokenResponseFilter.CodeRequest(loginProvider, code)))
                .contentType(APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/login/alreadyUser",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("loginProvider").type(JsonFieldType.STRING).description(generateLinkCode(PROVIDER)),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("oauth 서버로 부터 받은 인가 코드")
                                ),
                                responseHeaders(
                                        headerWithName(ACCESS_TOKEN_NAME).description("액세스 토큰"),
                                        headerWithName(REFRESH_TOKEN_NAME).description("리프레시 토큰")
                                )
                        )
                );
    }

    @Test
    void 사용자는_회원가입이_안되있을때_redirect_링크로_재요청을_보낼수있다() throws Exception {
        // given
        String code = "code";
        String loginProvider = "kakao";

        given(providerManager.authenticate(new OAuthAuthenticationToken(code, loginProvider)))
                .willThrow(new OAuth2AuthenticationException(new OAuth2Error("NOT_REGISTERED"), "1234"));

        // when
        ResultActions result = mvc.perform(post("/api/v1/login/oauth2/code")
                .content(mapper.writeValueAsBytes(new OAuthTokenResponseFilter.CodeRequest(loginProvider, code)))
                .contentType(APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/login/newUser",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("loginProvider").type(JsonFieldType.STRING).description(generateLinkCode(PROVIDER)),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("oauth 서버로 부터 받은 인가 코드")
                                ),
                                responseHeaders(
                                        headerWithName("cacheKey").description("oauth 서버로 부터 받은 사용자 정보 임시 저장 키")
                                )
                        )
                );
    }

}
