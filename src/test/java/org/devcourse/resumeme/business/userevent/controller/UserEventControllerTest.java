package org.devcourse.resumeme.business.userevent.controller;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.domain.Position.FRONT;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserEventControllerTest extends ControllerUnitTest {

    @Test
    void 멘티_마이페이지의_이력서를_조회한다() throws Exception {
        // given
        setMockData();

        // when
        ResultActions result = mvc.perform(get("/api/v1/mentees/{menteeId}/events", 1L));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("/user/mentee/mypage/resume",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("menteeId").description("멘티 아아디")
                                ),
                                responseFields(
                                        fieldWithPath("[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("[].title").type(STRING).description("이력서 제목"),
                                        fieldWithPath("[].lastModifiedDate").type(STRING).description("이력서 마지막 수정 시간"),
                                        fieldWithPath("[].events").type(ARRAY).description("해당 이력서로 참여한 이벤트 정보"),
                                        fieldWithPath("[].events[].mentorName").type(STRING).description("이벤트 생성 멘토 이름"),
                                        fieldWithPath("[].events[].eventTitle").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("[].events[].createdDate").type(STRING).description("이벤트 생성 날짜"),
                                        fieldWithPath("[].events[].status").type(STRING).description("이벤트 참여 상태")
                                )
                        )
                );
    }

    private void setMockData() throws NoSuchFieldException, IllegalAccessException {
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();
        EventInfo openEventOne = EventInfo.open(3, "수증 멘토의 첫번째 첨삭이벤트", "내용");
        EventTimeInfo eventOneTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));

        Event event1 = new Event(openEventOne, eventOneTimeInfo, mentor, List.of(FRONT));
        setId(event1, 1L);

        Event event2 = new Event(openEventOne, eventOneTimeInfo, mentor, List.of(FRONT));
        setId(event2, 2L);

        Field field1 = event1.getClass().getSuperclass().getDeclaredField("createdDate");
        field1.setAccessible(true);
        field1.set(event1, LocalDateTime.of(2023, 10, 17, 18, 30));
        Field field2 = event1.getClass().getSuperclass().getDeclaredField("createdDate");
        field2.setAccessible(true);
        field2.set(event2, LocalDateTime.of(2023, 10, 20, 18, 30));

        event1.acceptMentee(1L, 2L);
        event2.acceptMentee(1L, 3L);

        Mentee mentee = Mentee.builder()
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

        Resume resume1 = new Resume("title", mentee);
        Field field = resume1.getClass().getSuperclass().getDeclaredField("lastModifiedDate");
        field.setAccessible(true);
        field.set(resume1, LocalDateTime.of(2023, 11, 17, 18, 30));
        setId(resume1, 1L);
        Resume resume2 = resume1.copy();
        Resume resume3 = resume1.copy();
        setId(resume2, 2L);
        setId(resume3, 3L);

        given(resumeService.getAllByMenteeId(1L)).willReturn(List.of(resume1, resume2, resume3));
        given(eventService.getAll(List.of(2L, 3L))).willReturn(List.of(event1, event2));
    }

}
