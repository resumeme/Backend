package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LinkControllerTest extends ControllerUnitTest {

    @Test
    void 업무경험_저장에_성공한다() throws Exception {
        ResumeLinkRequest request = new ResumeLinkRequest("GITHUB", "https://github.com");
        Long resumeId = 1L;
        ReferenceLink entity = request.toEntity();
        Component component = entity.of(resumeId);

        given(componentService.create(component, "links")).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resumes/" + resumeId + "/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/link/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                                        fieldWithPath("url").type(STRING).description("링크 주소")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 링크 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 이력서_참고링크_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        ReferenceLink link = new ReferenceLink(LinkType.GITHUB, "https://github.com");

        Component component = link.of(resumeId);
        Component links = new Component("links", null, null, null, resumeId, List.of(component));
        setId(component, 1L);
        given(componentService.getAll(resumeId)).willReturn(List.of(links));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}/links", 1L));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/link/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                                        fieldWithPath("[].linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                                        fieldWithPath("[].url").type(STRING).description("링크 URL")
                                )

                        )
                );
    }

}
