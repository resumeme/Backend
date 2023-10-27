package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.ResumeCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.user.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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

public class ResumeControllerTest extends ControllerUnitTest {

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .email("email")
                .provider(Provider.KAKAO)
                .build();
    }

    @Test
    void 이력서_생성에_성공한다() throws Exception {
        ResumeCreateRequest request = new ResumeCreateRequest("title");
        Resume resume = request.toEntity(mentee);

        given(resumeService.create(resume)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 이력서 아이디")
                                )
                        )
                );
    }

}
