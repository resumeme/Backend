package org.devcourse.resumeme.business.user.controller.mentor;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.devcourse.resumeme.common.util.DocumentLinkGenerator;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.assertj.core.api.Assertions.INTEGER;
import static org.assertj.core.api.Assertions.MAP;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.ROLE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentorControllerTest extends ControllerUnitTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MentorRegisterInfoRequest mentorRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private Mentor mentor;

    private Mentor savedMentor;

    private Token token;

    @BeforeEach
    void setUp() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_PENDING);
        mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        mentor = mentorRegisterInfoRequest.toEntity(oAuth2TempInfo);
        token = new Token("issuedAccessToken", "issuedRefreshToken");
    }

    @Test
    void 멘토_회원_가입에_성공한다() throws Exception {
        // given
        savedMentor = Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(mentor.getRefreshToken())
                .requiredInfo(new RequiredInfo(mentor.getRequiredInfo().getRealName(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getPhoneNumber(), mentor.getRequiredInfo().getRole()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build();

        given(oAuth2InfoRedisService.getOne(any())).willReturn((oAuth2TempInfo));
        given(mentorService.create(any(Mentor.class))).willReturn(savedMentor);
        given(jwtService.createTokens(any(Claims.class))).willReturn(token);


        // when
        ResultActions result = mvc.perform(post("/api/v1/mentors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(mentorRegisterInfoRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh-Token"))
                .andDo(
                        document("user/mentor/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("cacheKey").type(STRING).description("임시 저장된 소셜로그인 값의 키"),
                                        fieldWithPath("requiredInfo.realName").type(STRING).description("이름(실명)"),
                                        fieldWithPath("requiredInfo.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("requiredInfo.phoneNumber").type(STRING).description("전화번호").attributes(constraints(" '-' 제외 숫자만")),
                                        fieldWithPath("requiredInfo.role").type(STRING).description(generateLinkCode(ROLE)).attributes(constraints("ROLE_PENDING")),
                                        fieldWithPath("experiencedPositions").type(ARRAY).description("활동 직무").description(generateLinkCode(DocumentLinkGenerator.DocUrl.POSITION)).optional(),
                                        fieldWithPath("careerYear").type(INTEGER).description("경력 연차").attributes(constraints("1 이상의 자연수")),
                                        fieldWithPath("careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional()
                                ),
                                responseHeaders(
                                        headerWithName("Authorization").description("액세스 토큰"),
                                        headerWithName("Refresh-Token").description("리프레시 토큰")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘토_정보_수정에_성공한다() throws Exception {
        // given
        Long mentorId = 1L;
        MentorInfoUpdateRequest request = new MentorInfoUpdateRequest("newNick", "01033323334", Set.of("FRONT"), "다양한 도메인에서 일한 경력이 있습니다.", 5, "안녕하세요~");
        given(menteeService.update(any(Long.class), any(MenteeInfoUpdateRequest.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/mentees/{mentorId}", mentorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/mentor/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("mentorId").description("멘토 id")
                                ),
                                requestFields(
                                        fieldWithPath("realName").type(STRING).description("실명"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호").attributes(constraints(" - 제외 숫자만")),
                                        fieldWithPath("experiencedPositions").type(ARRAY).description("활동 직무").description(generateLinkCode(DocumentLinkGenerator.DocUrl.POSITION)).optional(),
                                        fieldWithPath("careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("careerYear").type(NUMBER).description("경력 연차").attributes(constraints("1 이상의 자연수")),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(MAP).description("멘토 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘토_정보_조회에_성공한다 () throws Exception {
        // given
        savedMentor = Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(mentor.getRefreshToken())
                .requiredInfo(new RequiredInfo(mentor.getRequiredInfo().getRealName(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getPhoneNumber(), mentor.getRequiredInfo().getRole()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .introduce(mentor.getIntroduce())
                .build();

        given(mentorService.getOne(any(Long.class))).willReturn(savedMentor);

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentors/{mentorId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/mentor/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("mentorId").description("멘토 id")
                                ),
                                responseFields(
                                        fieldWithPath("imageUrl").type(STRING).description("프로필 이미지"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("role").type(STRING).description(generateLinkCode(ROLE)),
                                        fieldWithPath("experiencedPositions").type(ARRAY).description("활동 직무").description(generateLinkCode(DocumentLinkGenerator.DocUrl.POSITION)).optional(),
                                        fieldWithPath("careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional()
                                )
                        )
                );
    }

}
