package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.resume.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest extends ControllerUnitTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 트레이닝_저장에_성공한다() throws Exception {
        TrainingCreateRequest request = new TrainingCreateRequest(
                "인문학과", "학사", "학사 학위", LocalDate.of(2018, 3, 1),
                LocalDate.of(2022, 2, 28), 4.0, 4.5, "성적 우수"
        );
        Long resumeId = 1L;
        Training training = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(trainingService.create(training)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        result
                .andExpect(status().isOk())
                .andDo(
                        document("training/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("organization").type(STRING).description("교육 기관"),
                                        fieldWithPath("major").type(STRING).description("전공"),
                                        fieldWithPath("degree").type(STRING).description("학위"),
                                        fieldWithPath("admissionDate").type(STRING).description("입학일"),
                                        fieldWithPath("graduationDate").type(STRING).description("졸업일"),
                                        fieldWithPath("gpa").type(NUMBER).description("학점"),
                                        fieldWithPath("maxGpa").type(NUMBER).description("최고 학점"),
                                        fieldWithPath("explanation").type(STRING).description("설명")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 트레이닝 ID")
                                )
                        )
                );
    }
}


