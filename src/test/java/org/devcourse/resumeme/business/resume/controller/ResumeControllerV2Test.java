package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.v2.ResumeUpdateRequest;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumeControllerV2Test extends ControllerUnitTest {

    @Test
    void 이력서_제목_수정에_성공한다() throws Exception {
        // given
        ResumeUpdateRequest request = new ResumeUpdateRequest(null, null, null, null, "title");

        // when
        ResultActions result = mvc.perform(patch("/api/v2/resumes/{resumeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/v2/updateTitle",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                nullableHttpRequestSnippet(),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목"),
                                        fieldWithPath("memo").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("introduce").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("skills").type(ARRAY).description("이력서 제목").ignored(),
                                        fieldWithPath("position").type(STRING).description("이력서 제목").ignored()
                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    void 이력서_메모_수정에_성공한다() throws Exception {
        // given
        ResumeUpdateRequest request = new ResumeUpdateRequest(null, null, null, "memo", null);

        // when
        ResultActions result = mvc.perform(patch("/api/v2/resumes/{resumeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/v2/updateMemo",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                nullableHttpRequestSnippet(),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("memo").type(STRING).description("이력서 제목"),
                                        fieldWithPath("introduce").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("skills").type(ARRAY).description("이력서 제목").ignored(),
                                        fieldWithPath("position").type(STRING).description("이력서 제목").ignored()
                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    void 이력서_기본정조_수정에_성공한다() throws Exception {
        // given
        ResumeUpdateRequest request = new ResumeUpdateRequest("position", List.of("java", "spring boot"), "소개글", null, null);

        // when
        ResultActions result = mvc.perform(patch("/api/v2/resumes/{resumeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/v2/updateBasicInfo",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                nullableHttpRequestSnippet(),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("memo").type(STRING).description("이력서 제목").ignored(),
                                        fieldWithPath("introduce").type(STRING).description("이력서 제목"),
                                        fieldWithPath("skills").type(ARRAY).description("이력서 제목"),
                                        fieldWithPath("position").type(STRING).description("이력서 제목")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    void 이력서_피드백_반영에_성공한다() throws Exception {
        // given
        ResumeUpdateRequest request = new ResumeUpdateRequest(null, null, null, null, null);

        // when
        ResultActions result = mvc.perform(patch("/api/v2/resumes/{resumeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/v2/reflectFeedback",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                nullableHttpRequestSnippet(),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

}
