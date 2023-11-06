package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityRequestDto;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
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

        ActivityRequestDto request = new ActivityRequestDto(
                "활동1", startDate, endDate, false, "https://example.com", "활동 설명"
        );

        Long resumeId = 1L;
        Activity activity = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(activityService.create(activity)).willReturn(1L);

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
                                        fieldWithPath("activityName").type(STRING).description("활동명"),
                                        fieldWithPath("startDate").type(STRING).description("시작일"),
                                        fieldWithPath("endDate").type(STRING).description("종료일"),
                                        fieldWithPath("inProgress").type(BOOLEAN).description("진행 중 여부"),
                                        fieldWithPath("link").type(STRING).description("링크"),
                                        fieldWithPath("description").type(STRING).description("설명")
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
        Long resumeId = 1L;
        Activity activity = new Activity("Project A", LocalDate.now().minusMonths(6), LocalDate.now(), true, "https://projectalink.com", "Project A");
        Resume savedResume = resume.builder()
                .activity(List.of(activity))
                .build();

        given(resumeService.getOne(resumeId)).willReturn(savedResume);

        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/activities"));

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
