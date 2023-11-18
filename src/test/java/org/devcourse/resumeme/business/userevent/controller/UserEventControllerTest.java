package org.devcourse.resumeme.business.userevent.controller;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.EVENT_STATUS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
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

    private Mentee mentee;

    private Resume resume;

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

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);

        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
        setId(resume, 1L);
    }

    @Test
    @WithMockCustomUser(role = "ROLE_MENTOR")
    void 멘토_첨삭_이벤트_조회_성공한다() throws Exception {
        // given
        Long mentorId = 1L;

        given(eventService.getAll(new AllEventFilter(mentorId, null))).willReturn(List.of(event));
        given(eventPositionService.getAll(event.getId())).willReturn(List.of());
        given(resumeService.getAll(List.of(1L))).willReturn(List.of(resume));

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentors/{mentorId}/events", mentorId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/mentor/events",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("mentorId").description("멘토 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].info").type(OBJECT).description("이력서 아이디"),
                                        fieldWithPath("[].info.title").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].info.content").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].info.maximumCount").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].info.currentApplicantCount").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].info.status").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].info.positions").type(ARRAY).description("이력서 아이디"),
                                        fieldWithPath("[].info.timeInfo").type(OBJECT).description("이력서 아이디"),
                                        fieldWithPath("[].info.timeInfo.openDateTime").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].info.timeInfo.closeDateTime").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].info.timeInfo.endDate").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].resumes").type(ARRAY).description("이력서 아이디"),
                                        fieldWithPath("[].resumes[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].resumes[].menteeName").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].resumes[].resumeTitle").type(STRING).description("이력서 아이디"),
                                        fieldWithPath("[].resumes[].progressStatus").type(STRING).description("이력서 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티_첨삭_신청_내역_조회에_성공한다() throws Exception {
        // given
        Long menteeId = 1L;

        given(eventService.getAll(new AllEventFilter(null, menteeId))).willReturn(List.of(event));

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentees/{menteeId}/events", menteeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("user/mentee/events",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("[].title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("[].mentorName").type(STRING).description("이벤트 생성 멘토 이름"),
                                        fieldWithPath("[].startDate").type(STRING).description("이벤트 시작 날짜"),
                                        fieldWithPath("[].endDate").type(STRING).description("이벤트 종료 날짜")
                                )
                        )
                );
    }

}
