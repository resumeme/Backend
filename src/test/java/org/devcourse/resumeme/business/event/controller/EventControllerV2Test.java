package org.devcourse.resumeme.business.event.controller;

import org.devcourse.resumeme.business.event.controller.dto.v2.EventUpdateRequest;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerV2Test extends ControllerUnitTest {

    @Test
    void 첨삭_이벤트_신청을_반려한다() throws Exception {
        // given
        EventUpdateRequest request = new EventUpdateRequest(1L, "반려 메시지", null, null);
        doNothing().when(eventService).update(1L, request.toVo());

        // when
        ResultActions result = mvc.perform(patch("/api/v2/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/v2/reject",
                                getDocumentRequest(),
                                pathParameters(
                                        parameterWithName("eventId").description("이벤트 아이디")
                                ),
                                exceptionResponse(
                                        List.of(EVENT_REJECTED.name())
                                ),
                                requestFields(
                                        fieldWithPath("menteeId").type(NUMBER).description("이벤트 신청 반려 사유"),
                                        fieldWithPath("rejectMessage").type(STRING).description("이벤트 신청 반려 사유"),
                                        fieldWithPath("resumeId").type(NUMBER).description("이력서 아이디").ignored(),
                                        fieldWithPath("completeMessage").type(STRING).description("이벤트 리뷰 내용").ignored()
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 첨삭_이벤트_리뷰를_완료한다() throws Exception {
        // given
        EventUpdateRequest request = new EventUpdateRequest(null, null, 1L, "좋은 이력서에요.");
        doNothing().when(eventService).update(1L, request.toVo());

        // when
        ResultActions result = mvc.perform(patch("/api/v2/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/v2/completeReview",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("이벤트 아이디")
                                ),
                                exceptionResponse(
                                        List.of(EVENT_NOT_FOUND.name(), RESUME_NOT_FOUND.name())
                                ),
                                requestFields(
                                        fieldWithPath("menteeId").type(NUMBER).description("이벤트 신청 반려 사유").ignored(),
                                        fieldWithPath("rejectMessage").type(STRING).description("이벤트 신청 반려 사유").ignored(),
                                        fieldWithPath("resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("completeMessage").type(STRING).description("이벤트 리뷰 내용")
                                )
                        )
                );
    }

}