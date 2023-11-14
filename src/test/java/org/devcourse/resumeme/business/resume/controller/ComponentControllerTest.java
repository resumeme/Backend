package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
        ResultActions result = mvc.perform(delete("/api/v1/resume/{resumeId}/components/{componentId}", 1L, componentId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/component/delete",
                                getDocumentRequest(),
                                pathParameters(
                                        parameterWithName("resumeId").description("해당 이력서 아이디"),
                                        parameterWithName("componentId").description("삭제하고자 하는 블럭 아이디")
                                )
                        )
                );
    }

}