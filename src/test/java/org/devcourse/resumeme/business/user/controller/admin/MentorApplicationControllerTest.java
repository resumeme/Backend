package org.devcourse.resumeme.business.user.controller.admin;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplication;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType.ACCEPT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentorApplicationControllerTest extends ControllerUnitTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MentorRegisterInfoRequest mentorRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private String refreshToken;

    @BeforeEach
    void SetUP() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_PENDING);
        mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        refreshToken = "refreshTokenRecentlyIssued";
    }

    @Test
    void 멘토_승인요청을_확인한다() throws Exception {
        // given
        MentorApplication mentorApplication = new MentorApplication(
                Mentor.builder()
                        .id(1L)
                        .imageUrl(oAuth2TempInfo.getImageUrl())
                        .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                        .email(oAuth2TempInfo.getEmail())
                        .refreshToken(refreshToken)
                        .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), requiredInfoRequest.role()))
                        .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                        .careerContent(mentorRegisterInfoRequest.careerContent())
                        .careerYear(mentorRegisterInfoRequest.careerYear())
                        .build()
        );
        setId(mentorApplication, 1L);

        given(mentorApplicationService.getAll()).willReturn(List.of(mentorApplication));

        // when
        ResultActions result = mvc.perform(get("/api/v1/admin/applications"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/admin/allApplication",
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].applicationId").type(NUMBER).description("멘토 승인 신청 아이디"),
                                        fieldWithPath("[].mentorName").type(STRING).description("멘토 이름")
                                )
                        )
                );
    }


    @Test
    void 멘토_승인여부를_결정한다() throws Exception {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        long applicationId = 1L;
        long mentorId = 1L;
        ApplicationProcessType type = ACCEPT;

        given(mentorApplicationService.delete(applicationId)).willReturn(mentorId);
        doNothing().when(mentorService).updateRole(mentorId, type);
        given(mentorService.getOne(any())).willReturn(mentor);
        doNothing().when(emailService).sendEmail(any());

        // when
        ResultActions result = mvc.perform(delete("/api/v1/admin/applications/{applicationId}/{type}", applicationId, type.name()));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/admin/processApplication",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("applicationId").description("멘토 가입 후 신청시 발급 된 아이디"),
                                        parameterWithName("type").description("멘토가 생성한 이벤트 아이디")

                                )
                        )
                );
    }


}
