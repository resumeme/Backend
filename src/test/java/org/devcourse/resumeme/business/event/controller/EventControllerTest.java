package org.devcourse.resumeme.business.event.controller;

import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest.EventInfoRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest.EventTimeRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventUpdateRequest;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.business.event.service.vo.EventsFoundCondition;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.business.event.service.vo.AuthorizationRole.ALL;
import static org.devcourse.resumeme.common.domain.Position.BACK;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.EVENT_STATUS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.POSITION;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.global.exception.ExceptionCode.CAN_NOT_RESERVATION;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTOR_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RANGE_MAXIMUM_ATTENDEE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TIME_ERROR;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends ControllerUnitTest {

    private Mentor mentor;

    @BeforeEach
    void setUp() {
        mentor =  Mentor.builder()
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
    }

    @Test
    @WithMockCustomUser
    void 첨삭_이벤트_생성에_성공한다() throws Exception {
        // given
        EventInfoRequest eventInfoRequest = new EventInfoRequest("title", "content", 3);
        EventTimeRequest eventTimeRequest = new EventTimeRequest(
                LocalDateTime.of(2023, 10, 23, 12, 0),
                LocalDateTime.of(2023, 10, 23, 12, 0),
                LocalDateTime.of(2023, 10, 24, 12, 0),
                LocalDateTime.of(2023, 10, 30, 23, 0)
        );
        EventCreateRequest eventCreateRequest = new EventCreateRequest(eventInfoRequest, eventTimeRequest, List.of("FRONT", "BACK"));
        /* 로그인 기능 완료 후 멘토 주입 값 추후 변경 예정 */
        Event event = eventCreateRequest.toEntity(1L);

        given(eventService.create(event)).willReturn(1L);
        given(userService.getOne(1L)).willReturn(mentor.from());

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
                                exceptionResponse(List.of(MENTOR_NOT_FOUND.name(), NO_EMPTY_VALUE.name(), TIME_ERROR.name(), CAN_NOT_RESERVATION.name(), RANGE_MAXIMUM_ATTENDEE.name())),
                                requestFields(
                                        fieldWithPath("info.title").type(STRING).description("첨삭 이벤트 제목"),
                                        fieldWithPath("info.content").type(STRING).description("첨삭 이벤트 내용"),
                                        fieldWithPath("info.maximumAttendee").type(NUMBER).description("최대 첨삭 이벤트 참여 가능 멘티 인원"),
                                        fieldWithPath("time.now").type(STRING).description("첨삭 이벤트 생성 요청 시간"),
                                        fieldWithPath("time.openDateTime").type(STRING).description("첨삭 이벤트 신청 오픈 시간").optional().attributes(constraints("이벤트 오픈 예약 시 필수")),
                                        fieldWithPath("time.closeDateTime").type(STRING).description("첨삭 이벤트 신청 마감 시간").attributes(constraints("오픈 시간보다 느리게")),
                                        fieldWithPath("time.endDate").type(STRING).description("첨삭 종료 시간").optional().attributes(constraints("마감 시간보다 늦어야 함")),
                                        fieldWithPath("positions").type(ARRAY).description(generateLinkCode(POSITION))
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 첨삭 이벤트 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 첨삭_이벤트_수정에_성공한다() throws Exception {
        // given
        EventUpdateRequest.EventTimeRequest eventTimeRequest = new EventUpdateRequest.EventTimeRequest(
                LocalDateTime.of(2023, 10, 23, 12, 0),
                LocalDateTime.of(2023, 10, 24, 12, 0),
                LocalDateTime.of(2023, 10, 30, 23, 0)
        );
        EventUpdateRequest.EventInfoRequest eventInfoRequest = new EventUpdateRequest.EventInfoRequest("title", "content", 3);
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(eventInfoRequest, eventTimeRequest, List.of("BACK", "FRONT"));
        EventUpdateVo vo = eventUpdateRequest.toVo(1L);

        doNothing().when(eventService).update(vo);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(eventUpdateRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(NO_EMPTY_VALUE.name(), EVENT_NOT_FOUND.name(), NO_EMPTY_VALUE.name(), RANGE_MAXIMUM_ATTENDEE.name(), TIME_ERROR.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("수정할 이벤트 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("info.title").type(STRING).description("첨삭 이벤트 제목"),
                                        fieldWithPath("info.content").type(STRING).description("첨삭 이벤트 내용"),
                                        fieldWithPath("info.maximumAttendee").type(NUMBER).description("최대 첨삭 이벤트 참여 가능 멘티 인원"),
                                        fieldWithPath("time.openDateTime").type(STRING).description("첨삭 이벤트 신청 오픈 시간").optional().attributes(constraints("이벤트 오픈 예약 시 필수")),
                                        fieldWithPath("time.closeDateTime").type(STRING).description("첨삭 이벤트 신청 마감 시간").attributes(constraints("오픈 시간보다 느리게")),
                                        fieldWithPath("time.endDate").type(STRING).description("첨삭 종료 시간").optional().attributes(constraints("마감 시간보다 늦어야 함")),
                                        fieldWithPath("positions").type(ARRAY).description(generateLinkCode(POSITION))
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 첨삭 이벤트 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 첨삭_이벤트_재오픈에_성공한다() throws Exception {
        // given
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(null, null, null);
        EventUpdateVo vo = eventUpdateRequest.toVo(1L);

        doNothing().when(eventService).update(vo);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(eventUpdateRequest)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/reopen",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                nullableHttpRequestSnippet(),
                                exceptionResponse(List.of(NO_EMPTY_VALUE.name(), EVENT_NOT_FOUND.name(), NO_EMPTY_VALUE.name(), RANGE_MAXIMUM_ATTENDEE.name(), TIME_ERROR.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("수정할 이벤트 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("수정된 첨삭 이벤트 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser(role = "ROLE_MENTOR")
    void 첨삭_이벤트_상세_조회할수있다() throws Exception {
        // given
        Long eventId = 1L;
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 4L);
        setId(event, 1L);

        given(eventService.getOne(eventId)).willReturn(event);
        Resume resume1 = new Resume("title", 1L);
        Resume resume2 = new Resume("title", 2L);
        setId(resume1, 1L);
        setId(resume2, 4L);
        given(resumeService.getAll(List.of(1L, 4L))).willReturn(List.of(resume1, resume2));
        given(eventPositionService.getAll(eventId)).willReturn(List.of(new EventPosition(BACK, event, 1)));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events/{eventId}", eventId));

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("event/getOne",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(EVENT_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("멘토가 생성한 이벤트 아이디")
                                ),
                                exceptionResponse(
                                        List.of(EVENT_NOT_FOUND.name())
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("이벤트 아이디"),
                                        fieldWithPath("mentorId").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("content").type(STRING).description("이벤트 상세내용"),
                                        fieldWithPath("currentApplicantCount").type(NUMBER).description("현재 참여 인원 수"),
                                        fieldWithPath("maximumCount").type(NUMBER).description("참여 최대 인원 수"),
                                        fieldWithPath("positions").type(ARRAY).description(generateLinkCode(POSITION)),
                                        fieldWithPath("timeInfo").type(OBJECT).description("첨삭 관련 시간 정보"),
                                        fieldWithPath("timeInfo.openDateTime").type(STRING).description("첨삭 신청 시작 일"),
                                        fieldWithPath("timeInfo.closeDateTime").type(STRING).description("첨삭 신청 마감 일"),
                                        fieldWithPath("timeInfo.endDate").type(STRING).description("첨삭 종료 일")
                                )
                        )
                );
    }

    @Test
    void 이벤트_전체를_조회할수있다() throws Exception {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        setId(event, 1L);

        given(userInfoProvider.getByIds(List.of(1L))).willReturn(List.of(new UserResponse(1L, "nickname", "name", "email", "01012345678", "url")));
        given(eventService.getAllWithPage(new EventsFoundCondition(null, ALL), PageRequest.of(0, 10))).willReturn(new PageImpl<>(List.of(event)));
        given(eventPositionService.getAll(List.of(1L))).willReturn(List.of(new EventPosition(BACK, event, 1)));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events")
                .param("page", "0")
                .param("size", "10"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("event/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("events[].info").type(OBJECT).description("이벤트 정보"),
                                        fieldWithPath("events[].info.id").type(NUMBER).description("이벤트 아이디"),
                                        fieldWithPath("events[].info.mentorId").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("events[].info.title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("events[].info.content").type(STRING).description("이벤트 상세내용"),
                                        fieldWithPath("events[].info.maximumCount").type(NUMBER).description("참여 최대 인원 수"),
                                        fieldWithPath("events[].info.currentApplicantCount").type(NUMBER).description("현재 참여 인원 수"),
                                        fieldWithPath("events[].info.status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("events[].info.positions").type(ARRAY).description(generateLinkCode(POSITION)),
                                        fieldWithPath("events[].info.timeInfo").type(OBJECT).description("첨삭 관련 시간 정보"),
                                        fieldWithPath("events[].info.timeInfo.openDateTime").type(STRING).description("첨삭 신청 시작 일"),
                                        fieldWithPath("events[].info.timeInfo.closeDateTime").type(STRING).description("첨삭 신청 마감 일"),
                                        fieldWithPath("events[].info.timeInfo.endDate").type(STRING).description("첨삭 종료 일"),
                                        fieldWithPath("events[].mentorInfo").type(OBJECT).description("멘토 정보"),
                                        fieldWithPath("events[].mentorInfo.mentorId").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("events[].mentorInfo.nickname").type(STRING).description("멘토 닉네임"),
                                        fieldWithPath("events[].mentorInfo.imageUrl").type(STRING).description("멘토 프로필 주소"),
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
