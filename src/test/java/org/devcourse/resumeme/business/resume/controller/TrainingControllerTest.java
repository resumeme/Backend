package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.Training;
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

import static org.devcourse.resumeme.business.resume.domain.BlockType.TRAINING;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
    }

    @Test
    void 트레이닝_저장에_성공한다() throws Exception {
        // given
        TrainingCreateRequest request = new TrainingCreateRequest(
                "데브대", "컴퓨터공학과", "학사 학위", LocalDate.of(2018, 3, 1),
                LocalDate.of(2022, 2, 28), 4.0, 4.5, "성적 우수"
        );
        Long resumeId = 1L;
        Training training = request.toEntity();

        Component component = training.of(resumeId);

        given(componentService.create(component, TRAINING)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/training/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("organization").type(STRING).description("교육 기관"),
                                        fieldWithPath("major").type(STRING).description("전공"),
                                        fieldWithPath("degree").type(STRING).description("학위"),
                                        fieldWithPath("admissionDate").type(STRING).description("입학일"),
                                        fieldWithPath("graduationDate").type(STRING).description("졸업일"),
                                        fieldWithPath("gpa").type(NUMBER).description("학점").optional(),
                                        fieldWithPath("maxGpa").type(NUMBER).description("최고 학점").optional().attributes(constraints("gpa보다 큰 값이어야 함")),
                                        fieldWithPath("explanation").type(STRING).description("설명").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 트레이닝 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 교육사항_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        Training training = new Training("organization", "Computer Science", "Bachelor's", LocalDate.now(), LocalDate.now().plusYears(4), 3.8, 4.0, "Description");
        Component component = training.of(resumeId);

        Component training1 = new Component("TRAINING", null, null, null, resumeId, List.of(component));
        given(componentService.getAll(resumeId)).willReturn(List.of(training1));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/trainings"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/training/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].organization").type(STRING).description("기관명"),
                                        fieldWithPath("[].major").type(STRING).description("전공"),
                                        fieldWithPath("[].degree").type(STRING).description("학위"),
                                        fieldWithPath("[].admissionDate").type(STRING).description("입학일"),
                                        fieldWithPath("[].graduationDate").type(STRING).description("졸업일"),
                                        fieldWithPath("[].gpa").type(NUMBER).description("평점"),
                                        fieldWithPath("[].maxGpa").type(NUMBER).description("최고 평점"),
                                        fieldWithPath("[].explanation").type(STRING).description("설명")
                                )
                        )
                );
    }

}
