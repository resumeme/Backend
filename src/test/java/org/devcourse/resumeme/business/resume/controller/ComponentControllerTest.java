package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.activityCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.activitySnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.careerCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.careerSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.certificationCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.certificationSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.foreignLanguageCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.foreignLanguageSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.linkSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.projectCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.projectSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.resumeLinkRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.trainingCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.trainingSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.activityResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.activityResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.careerResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.careerResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.certificationResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.certificationResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.foreignLanguageResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.foreignLanguageResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.linkResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.projectResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.projectResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.referenceLinkResumeTemplate;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.trainingResponseSnippet;
import static org.devcourse.resumeme.business.resume.controller.ComponentResponse.trainingResumeTemplate;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.global.exception.ExceptionCode.COMPONENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ComponentControllerTest extends ControllerUnitTest {

    @Test
    void 블럭단위_삭제에_성공한다() throws Exception {
        // given
        long componentId = 1L;

        doNothing().when(componentService).delete(componentId);

        // when
        ResultActions result = mvc.perform(delete("/api/v1/resumes/{resumeId}/components/{componentId}", 1L, componentId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/delete",
                                getDocumentRequest(),
                                exceptionResponse(List.of(COMPONENT_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("해당 이력서 아이디"),
                                        parameterWithName("componentId").description("삭제하고자 하는 블럭 아이디")
                                ),
                                exceptionResponse(
                                        List.of(RESUME_NOT_FOUND.name(), COMPONENT_NOT_FOUND.name())
                                )
                        )
                );
    }

    @Test
    void 블럭_전체_조회에_성공한다() throws Exception {
        // given
        long resumeId = 1L;
        ResumeTemplate template = ResumeTemplate.builder()
                .activity(new ArrayList<>())
                .career(new ArrayList<>())
                .certification(new ArrayList<>())
                .foreignLanguage(new ArrayList<>())
                .project(new ArrayList<>())
                .training(new ArrayList<>())
                .referenceLink(new ArrayList<>())
                .build();

        given(componentService.getAll(1L, null)).willReturn(template);
        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}", resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/component/findAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회할 이력서 아이디")
                                ),
                                exceptionResponse(
                                        List.of(RESUME_NOT_FOUND.name())
                                )
                        )
                );
    }

    @ParameterizedTest(name = "component type : {1}")
    @MethodSource("componentRequest")
    void 블럭_생성에_성공한다(ComponentCreateRequest request, String type, RequestFieldsSnippet requestFields) throws Exception {
        // given
        given(componentService.create(getComponent(request), Property.valueOf(type.toUpperCase()))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/resumes/{resumeId}/{type}", 1L, type)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/create/%s".formatted(type),
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields,
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 활동 ID")
                                )
                        )
                );
    }

    @ParameterizedTest(name = "component type : {1}")
    @MethodSource("componentRequest")
    void 블럭_수정에_성공한다(ComponentCreateRequest request, String type, RequestFieldsSnippet requestFields) throws Exception {
        // given
        doNothing().when(componentService).delete(1L);
        given(componentService.create(getComponent(request), Property.valueOf(type.toUpperCase()))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/resumes/{resumeId}/{type}/components/{componentId}", 1L, type, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/update/%s".formatted(type),
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields,
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 활동 ID")
                                )
                        )
                );
    }

    @ParameterizedTest(name = "component type : {1}")
    @MethodSource("componentResponse")
    void 블럭_조회에_성공한다(ResumeTemplate request, String type, ResponseFieldsSnippet responseFields) throws Exception {
        // given
        given(componentService.getAll(1L, Property.valueOf(type.toUpperCase()))).willReturn(request);

        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}/{type}", 1L, type));

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/find/%s".formatted(type),
                                getDocumentRequest(),
                                getDocumentResponse(),
                                nullableHttpResponseSnippet(),
                                pathParameters(
                                    parameterWithName("resumeId").description("이력서 아이디"),
                                    parameterWithName("type").description(generateLinkCode(BLOCK_TYPE))
                                ),
                                responseFields
                        )
                );
    }

    private Component getComponent(ComponentCreateRequest request) {
        return request.toVo().toComponent(1L);
    }

    static Stream<Arguments> componentRequest() {
        return Stream.of(
                Arguments.of(activityCreateRequest(), "activities", activitySnippet()),
                Arguments.of(careerCreateRequest(), "careers", careerSnippet()),
                Arguments.of(certificationCreateRequest(), "certifications", certificationSnippet()),
                Arguments.of(foreignLanguageCreateRequest(), "foreignLanguages", foreignLanguageSnippet()),
                Arguments.of(resumeLinkRequest(), "links", linkSnippet()),
                Arguments.of(projectCreateRequest(), "projects", projectSnippet()),
                Arguments.of(trainingCreateRequest(), "trainings", trainingSnippet())
        );
    }

    static Stream<Arguments> componentResponse() throws NoSuchFieldException, IllegalAccessException {
        return Stream.of(
                Arguments.of(activityResumeTemplate(), "activities", activityResponseSnippet()),
                Arguments.of(careerResumeTemplate(), "careers", careerResponseSnippet()),
                Arguments.of(certificationResumeTemplate(), "certifications", certificationResponseSnippet()),
                Arguments.of(foreignLanguageResumeTemplate(), "foreignLanguages", foreignLanguageResponseSnippet()),
                Arguments.of(referenceLinkResumeTemplate(), "links", linkResponseSnippet()),
                Arguments.of(projectResumeTemplate(), "projects", projectResponseSnippet()),
                Arguments.of(trainingResumeTemplate(), "trainings", trainingResponseSnippet())
        );
    }

}
