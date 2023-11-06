package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.controller.dto.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.assertj.core.api.Assertions.MAP;
import static org.devcourse.resumeme.common.DocumentLinkGenerator.DocUrl.ROLE;
import static org.devcourse.resumeme.common.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MenteeControllerTest extends ControllerUnitTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MenteeRegisterInfoRequest menteeRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private Mentee mentee;

    @BeforeEach
    void setUp() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_MENTEE);
        menteeRegisterInfoRequest = new MenteeRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), Set.of("RETAIL", "MANUFACTURE"), "안녕하세요 백둥이 4기 머쓱이입니다.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "KAKAO", "지롱", "backdong1@kakao.com", "image.png");
        mentee = menteeRegisterInfoRequest.toEntity(oAuth2TempInfo, "refreshTokenRecentlyIssued");
    }

    @Test
    void 멘티_회원_가입에_성공한다() throws Exception {
        // given
        Mentee savedMentee = Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(mentee.getRefreshToken())
                .requiredInfo(mentee.getRequiredInfo())
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .build();

        given(oAuth2InfoRedisService.getOne(any())).willReturn(oAuth2TempInfo);
        given(menteeService.create(any(Mentee.class))).willReturn(savedMentee);
        given(jwtService.createAccessToken(any(Claims.class))).willReturn("accessTokenRecentlyIssued");
        given(jwtService.createRefreshToken()).willReturn("refreshTokenRecentlyIssued");

        // when
        ResultActions result = mvc.perform(post("/api/v1/mentees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menteeRegisterInfoRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().exists("access"))
                .andExpect(header().exists("refresh"))
                .andDo(
                        document("mentee/create",
                                getDocumentRequest(),
                                requestFields(
                                        fieldWithPath("cacheKey").type(STRING).description("임시 저장된 소셜로그인 값의 키"),
                                        fieldWithPath("requiredInfo.realName").type(STRING).description("이름(실명)"),
                                        fieldWithPath("requiredInfo.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("requiredInfo.phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("requiredInfo.role").type(STRING).description("역할"),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무"),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 도메인"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개")
                                ),
                                responseHeaders(
                                        headerWithName("access").description("액세스 토큰"),
                                        headerWithName("refresh").description("리프레시 토큰")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티_정보_조회에_성공한다() throws Exception {
        // given
        Long menteeId = 1L;

        Mentee savedMentee = Mentee.builder()
                .id(menteeId)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(mentee.getRefreshToken())
                .requiredInfo(mentee.getRequiredInfo())
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce("안녕하세요")
                .build();

        given(menteeService.getOne(any(Long.class))).willReturn(savedMentee);

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentees/{menteeId}", menteeId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("mentee/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 id")
                                ),
                                responseFields(
                                        fieldWithPath("imageUrl").type(STRING).description("프로필 이미지"),
                                        fieldWithPath("realName").type(STRING).description("실명"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("role").type(STRING).description(generateLinkCode(ROLE)),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무"),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 분야"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티_정보_수정에_성공한다() throws Exception {
        // given
        Long menteeId = 1L;
        MenteeInfoUpdateRequest request = new MenteeInfoUpdateRequest("newNick", "01033323334", Set.of("FRONT"), Set.of("RETAIL"), "안녕하세요!");
        given(menteeService.update(any(Long.class), any(MenteeInfoUpdateRequest.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/mentees/{menteeId}", menteeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("mentee/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 id")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무"),
                                        fieldWithPath("interestedFields").type(ARRAY).description("전화번호"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(MAP).description("멘티 아이디")
                                )
                        )
                );
    }

}
