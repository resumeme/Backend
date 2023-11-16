package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.COMPONENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ComponentControllerTest extends ControllerUnitTest {

    @Test
    void 블럭단위_삭제에_성공한다() throws Exception {
        // given
        long componentId = 1L;

        given(componentService.delete(componentId)).willReturn("activities");

        // when
        ResultActions result = mvc.perform(delete("/api/v1/resumes/{resumeId}/components/{componentId}", 1L, componentId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/delete",
                                getDocumentRequest(),
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


}
