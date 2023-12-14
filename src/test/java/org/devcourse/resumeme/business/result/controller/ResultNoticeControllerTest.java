package org.devcourse.resumeme.business.result.controller;

import org.devcourse.resumeme.business.result.controller.dto.CreateResultRequest;
import org.devcourse.resumeme.business.result.domain.ResultNotice;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResultNoticeControllerTest extends ControllerUnitTest {

    @Test
    void 합격_자소서_소개글을_작성한다() throws Exception {
        // given
        CreateResultRequest request = new CreateResultRequest(1L, "content");
        given(resumeService.getOne(request.resumeId())).willReturn(new Resume("title", 1L));
        given(resultService.create(any(ResultNotice.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("result/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                requestFields(
                                        fieldWithPath("resumeId").type(NUMBER).description("소개 할 합격 이력서 아이디"),
                                        fieldWithPath("content").type(STRING).description("작성 된 소개 글")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성 된 소개 글 아이디")
                                )
                        )
                );
    }

    @Test
    void 합격_자소서_전체를_페이징하여_본다() throws Exception {
        // given
        PageRequest request = PageRequest.of(1, 10);
        ResultNotice resultNotice = new ResultNotice("content", new Resume("title", 1L));
        setId(resultNotice, 1L);

        given(resultService.getAll(request)).willReturn(new PageImpl<>(
                List.of(resultNotice),
                request,
                1
        ));
        // when
        ResultActions result = mvc.perform(get("/api/v1/results")
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(10)));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("result/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("한 페이지에서 보여줄 갯수")
                                ),
                                responseFields(
                                        fieldWithPath("notice").type(ARRAY).description("결과 값"),
                                        fieldWithPath("notice[].resultId").type(NUMBER).description("합격 소개 글 아이디"),
                                        fieldWithPath("notice[].title").type(STRING).description("합격 소개 글 제목"),
                                        fieldWithPath("pageData").type(OBJECT).description("페이징 결과 값"),
                                        fieldWithPath("pageData.first").type(BOOLEAN).description("첫번째 페이지 여부"),
                                        fieldWithPath("pageData.last").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("pageData.number").type(NUMBER).description("몇 번째 페이지 번호"),
                                        fieldWithPath("pageData.size").type(NUMBER).description("한 페이지에 몇개 있는지"),
                                        fieldWithPath("pageData.sort").type(OBJECT).description("페이징 정렬"),
                                        fieldWithPath("pageData.sort.empty").type(BOOLEAN).description("페이징 정렬"),
                                        fieldWithPath("pageData.sort.sorted").type(BOOLEAN).description("페이징 정렬 여부"),
                                        fieldWithPath("pageData.sort.unsorted").type(BOOLEAN).description("페이징 정렬 여부"),
                                        fieldWithPath("pageData.totalPages").type(NUMBER).description("전체 페이지"),
                                        fieldWithPath("pageData.totalElements").type(NUMBER).description("전체 갯수")
                                )
                        )
                );
    }


}
