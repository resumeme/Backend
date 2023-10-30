package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.INTEGER;
import static org.assertj.core.api.Assertions.MAP;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentorControllerTest extends ControllerUnitTest {

    @Test
    void 멘토_회원_가입에_성공한다() throws Exception {
        // given
        RequiredInfoRequest requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_PENDING);
        MentorRegisterInfoRequest mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        OAuth2TempInfo oAuth2TempInfo = new OAuth2TempInfo("cacheKey", "GOOGLE", "지롱", "devcoco@naver.com", "image.png");

        Mentor mentor = mentorRegisterInfoRequest.toEntity(oAuth2TempInfo, "refreshTokenRecentlyIssued");
        Mentor savedMentor = Mentor.builder()
                .id(1L)
                .requiredInfo(mentor.getRequiredInfo())
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .build();

        given(oAuth2InfoRedisRepository.findById(any())).willReturn(Optional.of(oAuth2TempInfo));
        given(mentorService.create(any(Mentor.class))).willReturn(savedMentor);
        given(jwtService.createAccessToken(any(Claims.class))).willReturn("accessTokenRecentlyIssued");
        given(jwtService.createRefreshToken()).willReturn("refreshTokenRecentlyIssued");

        // when
        ResultActions result = mvc.perform(post("/api/v1/mentors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(mentorRegisterInfoRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("mentor/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("cacheKey").type(STRING).description("임시 저장된 소셜로그인 값의 키"),
                                        fieldWithPath("requiredInfo.realName").type(STRING).description("이름(실명)"),
                                        fieldWithPath("requiredInfo.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("requiredInfo.phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("requiredInfo.role").type(STRING).description("역할"),
                                        fieldWithPath("experiencedPositions").type(ARRAY).description("활동 직무"),
                                        fieldWithPath("careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개")
                                ),
                                responseFields(
                                        fieldWithPath("access").type(MAP).description("액세스 토큰"),
                                        fieldWithPath("refresh").type(MAP).description("리프레시 토큰")
                                )
                        )
                );
    }

}