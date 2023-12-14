package org.devcourse.resumeme.business.userevent.controller;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.EventsFoundCondition;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.business.event.service.vo.AuthorizationRole.MENTOR;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.EVENT_STATUS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.PROGRESS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserEventControllerTest extends ControllerUnitTest {

    private Mentor mentor;

    private Event event;

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
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

        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("progrers33@gmail.com")
                .refreshToken("refreshToken")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTEE))
                .interestedPositions(Set.of("FRONT"))
                .interestedFields(Set.of("FINANCE"))
                .introduce("백엔드 개발자")
                .build();

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        setId(event, 1L);
        event.acceptMentee(1L, 1L);

        resume = new Resume("title", 1L);
        setId(resume, 1L);
    }

    @Test
    @WithMockCustomUser(role = "ROLE_MENTOR")
    void 멘토_첨삭_이벤트_조회_성공한다() throws Exception {
        // given
        Long mentorId = 1L;

        given(eventService.getAllWithPage(new EventsFoundCondition(mentorId, MENTOR), Pageable.unpaged())).willReturn(new PageImpl<>(List.of(event)));
        given(resumeService.getAll(List.of(1L))).willReturn(List.of(resume));
        given(userInfoProvider.getByIds(List.of(1L))).willReturn(List.of(new UserResponse(1L, "nickname", "name", "email", "01012345678", "url")));

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentors/{mentorId}/events", mentorId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/mentor/events",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(BAD_REQUEST.name())),
                                pathParameters(
                                        parameterWithName("mentorId").description("멘토 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].info").type(OBJECT).description("이벤트 정보"),
                                        fieldWithPath("[].info.id").type(NUMBER).description("이벤트 아이디"),
                                        fieldWithPath("[].info.mentorId").type(NUMBER).description("멘토아이디"),
                                        fieldWithPath("[].info.title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("[].info.content").type(STRING).description("이벤트 내용"),
                                        fieldWithPath("[].info.maximumCount").type(NUMBER).description("이벤트 최대 참여 인원 수"),
                                        fieldWithPath("[].info.currentApplicantCount").type(NUMBER).description("이벤트 현재 참여 인원 수"),
                                        fieldWithPath("[].info.status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("[].info.positions").type(ARRAY).description("이벤트 참여 포지션"),
                                        fieldWithPath("[].info.timeInfo").type(OBJECT).description("이벤트 시간 정보"),
                                        fieldWithPath("[].info.timeInfo.openDateTime").type(STRING).description("이벤트 첨삭 참여 오픈 시간"),
                                        fieldWithPath("[].info.timeInfo.closeDateTime").type(STRING).description("이벤트 첨삭 참여 종료 시간"),
                                        fieldWithPath("[].info.timeInfo.endDate").type(STRING).description("이벤트 첨삭 종료 시간"),
                                        fieldWithPath("[].resumes").type(ARRAY).description("이력서 정보"),
                                        fieldWithPath("[].resumes[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].resumes[].menteeName").type(STRING).description("이력서 작성 멘티 이름"),
                                        fieldWithPath("[].resumes[].resumeTitle").type(STRING).description("이력서 제목"),
                                        fieldWithPath("[].resumes[].progressStatus").type(STRING).description(generateLinkCode(PROGRESS))
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티_첨삭_신청_내역_조회에_성공한다() throws Exception {
        // given
        Long menteeId = 1L;

        given(menteeToEventService.getByMenteeId(menteeId)).willReturn(List.of(new MenteeToEvent(event, menteeId, 1L)));
        given(userInfoProvider.getByIds(List.of(1L))).willReturn(List.of(new UserResponse(1L, "nickname", "name", "email", "01012345678", "url")));
        given(resumeService.getAll(List.of(1L))).willReturn(List.of(resume));
        // when
        ResultActions result = mvc.perform(get("/api/v1/mentees/{menteeId}/events", menteeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/mentee/events",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(BAD_REQUEST.name())),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].eventId").type(NUMBER).description("이벤트 아이디"),
                                        fieldWithPath("[].resumeTitle").type(STRING).description("이력서 제목"),
                                        fieldWithPath("[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("[].title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("[].mentorName").type(STRING).description("이벤트 생성 멘토 이름"),
                                        fieldWithPath("[].startDate").type(STRING).description("이벤트 시작 날짜"),
                                        fieldWithPath("[].endDate").type(STRING).description("이벤트 종료 날짜"),
                                        fieldWithPath("[].rejectMessage").type(STRING).description("반려 사요")
                                )
                        )
                );
    }

}
