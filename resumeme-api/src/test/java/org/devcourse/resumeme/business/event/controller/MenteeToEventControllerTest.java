package org.devcourse.resumeme.business.event.controller;

import org.devcourse.resumeme.business.event.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.v2.ApplyUpdateRequest;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.DUPLICATE_APPLICATION_EVENT;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_REJECTED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_REMAIN_SEATS;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenteeToEventControllerTest extends ControllerUnitTest {

    @Test
    @WithMockCustomUser
    void 참여_여부를_확인한다() throws Exception {
        // given
        long eventId = 1L;

        // when
        ResultActions result = mvc.perform(get("/api/v1/appliments/events/{eventId}", eventId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/record",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("이벤트 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("참여 아이디 (미 참여시 null 값으로 나갑니다)")
                                )
                        )
                );
    }

    @Test
    void 첨삭_이벤트_신청을_반려한다() throws Exception {
        // given
        ApplyUpdateRequest request = new ApplyUpdateRequest(1L, "반려 메시지", null, null);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/appliments/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/v2/reject",
                                getDocumentRequest(),
                                exceptionResponse(List.of(EVENT_NOT_FOUND.name(), MENTEE_NOT_FOUND.name())),
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
        ApplyUpdateRequest request = new ApplyUpdateRequest(null, null, 1L, "좋은 이력서에요.");

        // when
        ResultActions result = mvc.perform(patch("/api/v1/appliments/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/v2/completeReview",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(EVENT_NOT_FOUND.name(), RESUME_NOT_FOUND.name(), EVENT_REJECTED.name())),
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

    @Test
    @WithMockCustomUser
    void 첨삭_이벤트_참여에_성공한다() throws Exception {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("progrers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        event.acceptMentee(1L, 1L);

        ApplyToEventRequest request = new ApplyToEventRequest(1L);
        doNothing().when(menteeToEventService).acceptMentee(new AcceptMenteeToEvent(1L, 1L, 1L));

        // when
        ResultActions result = mvc.perform(post("/api/v1/appliments/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/apply",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name(), DUPLICATE_APPLICATION_EVENT.name(), NO_REMAIN_SEATS.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("참여하고 싶은 이벤트 아이디")
                                ),
                                exceptionResponse(
                                        List.of(EVENT_NOT_FOUND.name())
                                ),
                                requestFields(
                                        fieldWithPath("resumeId").type(NUMBER).description("이벤트 참여시 사용할 이력서 아이디")
                                )
                        )
                );
    }

}
