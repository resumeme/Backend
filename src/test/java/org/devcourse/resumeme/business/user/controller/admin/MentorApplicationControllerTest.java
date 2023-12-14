package org.devcourse.resumeme.business.user.controller.admin;

import org.devcourse.resumeme.business.user.controller.dto.admin.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentor.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplication;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.business.user.controller.dto.admin.ApplicationProcessType.ACCEPT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MentorApplicationControllerTest extends ControllerUnitTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MentorRegisterInfoRequest mentorRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private String refreshToken;

    @BeforeEach
    void SetUP() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "전현무", "01034548443", "pending");
        mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        refreshToken = "refreshTokenRecentlyIssued";
    }

    @Test
    void 멘토_승인요청을_확인한다() throws Exception {
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

        MentorApplication mentorApplication = new MentorApplication(1L);
        setId(mentorApplication, 1L);

        given(mentorApplicationService.getAll()).willReturn(List.of(mentorApplication));
        given(userService.getByIds(List.of(1L))).willReturn(List.of(mentor.from()));
        // when
        ResultActions result = mvc.perform(get("/admin/applications"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("mentorApprovalPage"));
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
        doNothing().when(userService).updateRole(mentorId, type);
        given(userService.getOne(any())).willReturn(mentor.from());
        doNothing().when(emailService).sendEmail(any());

        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post("/admin/applications/{applicationId}/approve", applicationId));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/applications"));
    }

}
