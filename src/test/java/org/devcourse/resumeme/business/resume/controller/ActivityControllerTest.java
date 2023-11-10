package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ActivityControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("image.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("devcourse@naver.com")
                .refreshToken("fjiejrwoosdfsfsddfss")
                .requiredInfo(new RequiredInfo("이동호", "동동", "01022283833", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
    }

    @Test
    void 활동_저장에_성공한다() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        ActivityCreateRequest request = new ActivityCreateRequest(
                "활동1", startDate, endDate, false, "https://example.com", "활동 설명"
        );

        Long resumeId = 1L;
        Activity activity = request.toEntity();

        Component component = activity.of(resumeId);

        given(componentService.create(component, BlockType.CAREER)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/activity/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("activityName").type(STRING).description("활동명"),
                                        fieldWithPath("startDate").type(STRING).description("시작일"),
                                        fieldWithPath("endDate").type(STRING).description("종료일"),
                                        fieldWithPath("inProgress").type(BOOLEAN).description("진행 중 여부").optional(),
                                        fieldWithPath("link").type(STRING).description("링크").optional(),
                                        fieldWithPath("description").type(STRING).description("설명").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 활동 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 활동_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        Activity activity = new Activity("Project A", LocalDate.now().minusMonths(6), LocalDate.now(), "https://projectalink.com", "Project A");
        Component component = activity.of(resumeId);

        Component activity1 = new Component("ACTIVITY", null, null, null, resumeId, List.of(component));
        given(componentService.getAll(resumeId)).willReturn(List.of(activity1));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/activities"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/activity/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].activityName").type(STRING).description("활동명"),
                                        fieldWithPath("[].startDate").type(STRING).description("시작일"),
                                        fieldWithPath("[].endDate").type(STRING).description("종료일"),
                                        fieldWithPath("[].inProgress").type(BOOLEAN).description("진행 중 여부"),
                                        fieldWithPath("[].link").type(STRING).description("링크"),
                                        fieldWithPath("[].description").type(STRING).description("설명")
                                )
                        )
                );
    }

}
