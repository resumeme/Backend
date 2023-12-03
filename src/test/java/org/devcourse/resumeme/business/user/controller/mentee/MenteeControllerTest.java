package org.devcourse.resumeme.business.user.controller.mentee;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentee.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentee.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.service.vo.CreatedUserVo;
import org.devcourse.resumeme.business.user.service.vo.UpdateMenteeVo;
import org.devcourse.resumeme.business.user.service.vo.UserDomainVo;
import org.devcourse.resumeme.business.user.service.vo.UserInfoVo;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.MAP;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.ROLE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.global.auth.service.jwt.Token.ACCESS_TOKEN_NAME;
import static org.devcourse.resumeme.global.auth.service.jwt.Token.REFRESH_TOKEN_NAME;
import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INFO_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_EMAIL;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ROLE_NOT_ALLOWED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TEXT_OVER_LENGTH;
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

    private Token token;

    @BeforeEach
    void setUp() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "김백둥", "01034548443", "mentee");
        menteeRegisterInfoRequest = new MenteeRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), Set.of("COMMERCE", "MANUFACTURE"), "안녕하세요 백둥이 4기 머쓱이입니다.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "KAKAO", "지롱", "backdong1@kakao.com", "image.png");
        UserDomainVo vo = menteeRegisterInfoRequest.toVo(oAuth2TempInfo);
        mentee = Mentee.of(vo.toUser());
        token = new Token("issuedAccessToken", "issuedRefreshToken");
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

        User savedUser = savedMentee.from();

        given(accountService.getTempInfo(any())).willReturn(oAuth2TempInfo);
        given(accountService.registerAccount(any())).willReturn(token);
        given(userService.create(any(User.class))).willReturn(CreatedUserVo.of(savedUser));

        // when
        ResultActions result = mvc.perform(post("/api/v1/mentees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menteeRegisterInfoRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().exists(ACCESS_TOKEN_NAME))
                .andExpect(header().exists(REFRESH_TOKEN_NAME))
                .andDo(
                        document("user/mentee/create",
                                getDocumentRequest(),
                                exceptionResponse(
                                        List.of(INVALID_EMAIL.name(), NO_EMPTY_VALUE.name(), ROLE_NOT_ALLOWED.name(), TEXT_OVER_LENGTH.name(), INFO_NOT_FOUND.name(), "INVALID_TEXT", INFO_NOT_FOUND.name())
                                ),
                                requestFields(
                                        fieldWithPath("cacheKey").type(STRING).description("임시 저장된 소셜로그인 값의 키"),
                                        fieldWithPath("requiredInfo.realName").type(STRING).description("이름(실명)").attributes(constraints("6자 이하의 한글")),
                                        fieldWithPath("requiredInfo.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("requiredInfo.phoneNumber").type(STRING).description("전화번호").attributes(constraints(" '-' 제외 숫자만")),
                                        fieldWithPath("requiredInfo.role").type(STRING).description(generateLinkCode(ROLE)).attributes(constraints("ROLE_MENTEE")),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무").description(generateLinkCode(DocUrl.POSITION)).optional(),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 도메인").description(generateLinkCode(DocUrl.FIELD)).optional(),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional().attributes(constraints("100자 이하"))
                                ),
                                responseHeaders(
                                        headerWithName(ACCESS_TOKEN_NAME).description("액세스 토큰"),
                                        headerWithName(REFRESH_TOKEN_NAME).description("리프레시 토큰")
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

        User savedUser = savedMentee.from();

        given(userService.getOne(Role.ROLE_MENTEE, 1L)).willReturn(new UserInfoVo(Mentee.of(savedUser)));

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentees/{menteeId}", menteeId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/mentee/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(
                                        List.of(MENTEE_NOT_FOUND.name(), BAD_REQUEST.name())
                                ),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 id")
                                ),
                                responseFields(
                                        fieldWithPath("id").ignored(),
                                        fieldWithPath("role").ignored(),
                                        fieldWithPath("imageUrl").type(STRING).description("프로필 이미지"),
                                        fieldWithPath("realName").type(STRING).description("실명"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무").description(generateLinkCode(DocUrl.POSITION)).optional(),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 도메인").description(generateLinkCode(DocUrl.FIELD)).optional(),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional(),
                                        fieldWithPath("experiencedPositions").ignored(),
                                        fieldWithPath("careerContent").ignored(),
                                        fieldWithPath("careerYear").ignored()
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티_정보_수정에_성공한다() throws Exception {
        // given
        Long menteeId = 1L;
        MenteeInfoUpdateRequest request = new MenteeInfoUpdateRequest("newNick", "01033323334", Set.of("FRONT"), Set.of("SNS"), "안녕하세요!");
        given(userService.update(any(Long.class), any(UpdateMenteeVo.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/mentees/{menteeId}", menteeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/mentee/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(
                                        List.of(INVALID_EMAIL.name(), NO_EMPTY_VALUE.name(), ROLE_NOT_ALLOWED.name(), TEXT_OVER_LENGTH.name(), "INVALID_TEXT", BAD_REQUEST.name(), MENTEE_NOT_FOUND.name())
                                ),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 id")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호").attributes(constraints(" '-' 제외 숫자만")),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무").description(generateLinkCode(DocUrl.POSITION)).optional(),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 도메인").description(generateLinkCode(DocUrl.FIELD)).optional(),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional().attributes(constraints("100자 이하"))
                                ),
                                responseFields(
                                        fieldWithPath("id").type(MAP).description("멘티 아이디")
                                )
                        )
                );
    }

}
