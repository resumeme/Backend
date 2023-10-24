package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.controller.dto.EventCreateRequest.EventInfoRequest;
import org.devcourse.resumeme.controller.dto.EventCreateRequest.EventTimeRequest;
import org.devcourse.resumeme.controller.dto.UpdateEventMaxCountRequest;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.devcourse.resumeme.common.DocumentLinkGenerator.DocUrl.POSITION;
import static org.devcourse.resumeme.common.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends ControllerUnitTest {

    @Test
    void 첨삭_이벤트_생성에_성공한다() throws Exception {
        // given
        EventInfoRequest eventInfoRequest = new EventInfoRequest("title", "content", 3);
        EventTimeRequest eventTimeRequest = new EventTimeRequest(
                LocalDateTime.of(2023,10,23,12,0),
                LocalDateTime.of(2023,10,23,12,0),
                LocalDateTime.of(2023,10,24,12,0),
                LocalDateTime.of(2023,10,30,23,0)
        );
        EventCreateRequest eventCreateRequest = new EventCreateRequest(eventInfoRequest, eventTimeRequest, List.of("FRONT", "BACK"));
        /* 로그인 기능 완료 후 멘토 주입 값 추후 변경 예정 */
        Event event = eventCreateRequest.toEntity(new Mentor());

        given(eventService.create(event)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(eventCreateRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("info.title").type(STRING).description("첨삭 이벤트 제목"),
                                        fieldWithPath("info.content").type(STRING).description("첨삭 이벤트 내용"),
                                        fieldWithPath("info.maximumAttendee").type(NUMBER).description("최대 첨삭 이벤트 참여 가능 멘티 인원"),
                                        fieldWithPath("time.now").type(STRING).description("첨삭 이벤트 생성 요청 시간"),
                                        fieldWithPath("time.openDateTime").type(STRING).description("첨삭 이벤트 신청 오픈 시간"),
                                        fieldWithPath("time.closeDateTime").type(STRING).description("첨삭 이벤트 신청 마감 시간"),
                                        fieldWithPath("time.endDate").type(STRING).description("첨삭 종료 시간"),
                                        fieldWithPath("positions").type(ARRAY).description(generateLinkCode(POSITION))
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 첨삭 이벤트 아이디")
                                )
                        )
                );
    }

    @Test
    void 첨삭_이벤트_참여인원수를_변경할_수_있다() throws Exception {
        // given
        long eventId = 1L;
        int newMaxCount = 10;
        UpdateEventMaxCountRequest request = new UpdateEventMaxCountRequest(newMaxCount);
        given(eventService.updateMaximumAttendeeCount(eventId, request.count())).willReturn(newMaxCount);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("event/updateMaxCount",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("count").type(NUMBER).description("새로 지정할 인원 수")
                                ),
                                responseFields(
                                        fieldWithPath("remainCount").type(NUMBER).description("변경된 인원 수 반영한 남은 자리 수"),
                                        fieldWithPath("maxCount").type(NUMBER).description("새로 변경 된 참여 가능 인원 수")
                                )
                        )
                );
    }


}
