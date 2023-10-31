package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.ForeignLanguageRequestDto;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.ForeignLanguage;
import org.devcourse.resumeme.domain.resume.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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

class ForeignLanguageControllerTest extends ControllerUnitTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 외국어_저장에_성공한다() throws Exception {
        ForeignLanguageRequestDto request = new ForeignLanguageRequestDto("English", "TOEIC", "900");
        Long resumeId = 1L;
        ForeignLanguage foreignLanguage = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(foreignLanguageService.create(foreignLanguage)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/foreign-languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        result
                .andExpect(status().isOk())
                .andDo(
                        document("foreignLanguage/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("language").type(STRING).description("언어"),
                                        fieldWithPath("examName").type(STRING).description("시험명"),
                                        fieldWithPath("scoreOrGrade").type(STRING).description("점수 또는 학점")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 외국어 정보 ID")
                                )
                        )
                );
    }
}

