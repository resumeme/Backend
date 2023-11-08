package org.devcourse.resumeme.business.event.controller;

import org.devcourse.resumeme.business.event.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest.EventInfoRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest.EventTimeRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventRejectRequest;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.EventReject;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.domain.Position.BACK;
import static org.devcourse.resumeme.common.domain.Position.FRONT;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.EVENT_STATUS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.POSITION;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.PROGRESS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends ControllerUnitTest {

    private Mentor mentor;

    private Mentee mentee1;

    private Mentee mentee2;

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

        mentee1 = Mentee.builder()
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

        mentee2 = Mentee.builder()
                .id(2L)
                .imageUrl("menteeimage2.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong2@kakao.com")
                .refreshToken("ddefwefer2frte")
                .requiredInfo(new RequiredInfo("김백둥2", "백둥둥2", "01072223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
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
        Event event = eventCreateRequest.toEntity(mentor);

        given(eventService.create(event)).willReturn(1L);
        given(mentorService.getOne(1L)).willReturn(mentor);

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
    void 첨삭_이벤트_참여에_성공한다() throws Exception {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);

        ApplyToEventRequest request = new ApplyToEventRequest(1L);
        given(eventService.acceptMentee(new AcceptMenteeToEvent(1L, 1L, 1L))).willReturn(event);
        given(eventService.getApplicantId(event, 1L)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/apply",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("참여하고 싶은 이벤트 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("resumeId").type(NUMBER).description("이벤트 참여시 사용할 이력서 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("이벤트에 참여 성공후 발급된 이력 아이디")
                                )
                        )
                );
    }

    @Test
    void 첨삭_이벤트_신청을_반려한다() throws Exception {
        // given
        EventRejectRequest request = new EventRejectRequest("이력서 작성 양이 너무 적습니다");
        doNothing().when(eventService).reject(new EventReject(1L, 1L, "이력서 작성 양이 너무 적습니다"));

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}/mentee/{menteeId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/reject",
                                getDocumentRequest(),
                                pathParameters(
                                        parameterWithName("eventId").description("이벤트 아이디"),
                                        parameterWithName("menteeId").description("반려시키고 싶은 멘티 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("rejectMessage").type(STRING).description("이벤트 신청 반려 사유").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 리뷰_재_요청을_신청한다() throws Exception {
        // given
        Long eventId = 1L;

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}/resume/{resumeId}/mentee", eventId, 1L));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("event/requestReview",
                                getDocumentRequest(),
                                getDocumentResponse()
                        )
                );
    }

    @Test
    @WithMockCustomUser(role = "ROLE_MENTOR")
    void 첨삭_이벤트에_참여한_이력서를_조회할수있다() throws Exception {
        // given
        Long eventId = 1L;
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 4L);

        given(eventService.getOne(eventId)).willReturn(event);
        Resume resume1 = new Resume("title", mentee1);
        Resume resume2 = new Resume("title", mentee2);
        setId(resume1, 1L);
        setId(resume2, 4L);
        given(resumeService.getAll(List.of(1L, 4L))).willReturn(List.of(resume1, resume2));
        given(eventPositionService.getAll(eventId)).willReturn(List.of(new EventPosition(BACK, event)));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events/{eventId}", eventId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("event/getOwn",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("멘토가 생성한 이벤트 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("info").type(OBJECT).description("이벤트 관련 정보"),
                                        fieldWithPath("info.title").type(STRING).description("이벤트 제목"),
                                        fieldWithPath("info.content").type(STRING).description("이벤트 상세내용"),
                                        fieldWithPath("info.currentApplicantCount").type(NUMBER).description("현재 참여 인원 수"),
                                        fieldWithPath("info.maximumCount").type(NUMBER).description("참여 최대 인원 수"),
                                        fieldWithPath("info.positions").type(ARRAY).description(generateLinkCode(POSITION)),
                                        fieldWithPath("info.timeInfo").type(OBJECT).description("첨삭 관련 시간 정보"),
                                        fieldWithPath("info.timeInfo.openDateTime").type(STRING).description("첨삭 신청 시작 일"),
                                        fieldWithPath("info.timeInfo.closeDateTime").type(STRING).description("첨삭 신청 마감 일"),
                                        fieldWithPath("info.timeInfo.endDate").type(STRING).description("첨삭 종료 일"),
                                        fieldWithPath("resumes").type(ARRAY).description("이벤트 신청에 사용된 이력서 멘토 로그인 시에만 값 제공"),
                                        fieldWithPath("resumes[].resumeId").type(NUMBER).description("이력서 아이디"),
                                        fieldWithPath("resumes[].menteeName").type(STRING).description("이력서 작성 멘티 이름"),
                                        fieldWithPath("resumes[].resumeTitle").type(STRING).description("이력서 제목"),
                                        fieldWithPath("resumes[].progressStatus").type(STRING).description(generateLinkCode(PROGRESS))
                                )
                        )
                );
    }

    @Test
    void 첨삭_이벤트를_전체_조회한다() throws Exception {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        RequiredInfoRequest requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_PENDING);
        MentorRegisterInfoRequest mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        OAuth2TempInfo oAuth2TempInfo = new OAuth2TempInfo(null,"GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        String refreshToken = "refreshTokenRecentlyIssued";
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), requiredInfoRequest.role()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build();

        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of(BACK, FRONT));
        setId(event, 1L);

        given(eventService.getAll()).willReturn(List.of(event));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("event/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].eventInfo").type(OBJECT).description("이벤트 정보 객체"),
                                        fieldWithPath("[].eventInfo.eventId").type(NUMBER).description("이벤트 아이디"),
                                        fieldWithPath("[].eventInfo.title").type(STRING).description("이벤트 아이디"),
                                        fieldWithPath("[].eventInfo.endDate").type(STRING).description("이벤트 아이디"),
                                        fieldWithPath("[].eventInfo.status").type(STRING).description(generateLinkCode(EVENT_STATUS)),
                                        fieldWithPath("[].eventInfo.positions").type(ARRAY).description(generateLinkCode(POSITION)),
                                        fieldWithPath("[].mentorInfo").type(OBJECT).description("이벤트 생성 멘토 아이디"),
                                        fieldWithPath("[].mentorInfo.mentorId").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("[].mentorInfo.nickname").type(STRING).description("멘토 닉네임"),
                                        fieldWithPath("[].mentorInfo.imageUrl").type(STRING).description("멘토 프로필 사진 주소")
                                )
                        )
                );
    }

}
