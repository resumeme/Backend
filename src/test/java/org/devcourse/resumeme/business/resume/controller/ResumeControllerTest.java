package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeInfoRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeRequest;
import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.ResumeInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.domain.Position.*;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.EVENT_STATUS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.POSITION;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumeControllerTest extends ControllerUnitTest {

    private Mentee mentee;

    private Resume resume;

    @BeforeEach
    void init() {
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
    }

    @Test
    @WithMockCustomUser
    void 이력서_생성에_성공한다() throws Exception {
        ResumeRequest request = new ResumeRequest("title");
        Resume resume = request.toEntity(mentee);

        given(menteeService.getOne(any())).willReturn(mentee);
        given(resumeService.create(resume)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    void 이력서_기본정보_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;

        Resume resume = Resume.builder()
                .title("title")
                .resumeInfo(new ResumeInfo("백엔드", List.of("자바", "스프링"), "안녕하세요"))
                .mentee(mentee)
                .build();
        given(resumeService.getOne(resumeId)).willReturn(resume);

        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}", resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/basicInfo",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("이력서 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목"),
                                        fieldWithPath("position").type(STRING).description("지원 분야"),
                                        fieldWithPath("skills").type(ARRAY).description("사용 기술"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개글"),
                                        fieldWithPath("ownerInfo").type(OBJECT).description("작성자 정보"),
                                        fieldWithPath("ownerInfo.id").type(NUMBER).description("작성자 아이디"),
                                        fieldWithPath("ownerInfo.name").type(STRING).description("작성자 이름"),
                                        fieldWithPath("ownerInfo.phoneNumber").type(STRING).description("작성자 핸드폰 번호")
                                )
                        )
                );
    }


    @Test
    @WithMockCustomUser
    void 이력서_업데이트에_성공한다() throws Exception {
        ResumeInfoRequest request = new ResumeInfoRequest("BACK", List.of("Java", "Spring"), "안녕하세요 blah blah");
        Long resumeId = 1L;

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(resumeService.updateResumeInfo(resume, request.toEntity())).willReturn(1L);

        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/resumes/{resumeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("position").description("포지션").optional(),
                                        fieldWithPath("skills").description("스킬 목록").optional(),
                                        fieldWithPath("introduce").description("자기 소개").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 이력서_제목_수정에_성공한다() throws Exception {
        ResumeRequest request = new ResumeRequest("new title");
        Long resumeId = 1L;

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(resumeService.updateTitle(resume, request.title())).willReturn(1L);

        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/resumes/{resumeId}/title", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/updateTitle",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목")

                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 이력서_참고링크_조회에_성공한다() throws Exception {
        Long resumeId = 1L;
        ReferenceLink referenceLink = new ReferenceLink(LinkType.BLOG, "resumeme.tistory.com");
        Resume savedResume = resume.builder().
                referenceLink(referenceLink)
                .build();

        given(resumeService.getOne(resumeId)).willReturn(savedResume);

        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}/link", 1L));

        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/findLink",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("조회 이력서 id")
                                ),
                                responseFields(
                                        fieldWithPath("linkType").type(STRING).description("링크 유형(깃허브 주소, 블로그 주소, 기타)"),
                                        fieldWithPath("url").type(STRING).description("링크 URL")
                                )

                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 이력서_참고링크_수정에_성공한다() throws Exception {
        ResumeLinkRequest request = new ResumeLinkRequest("GITHUB", "https://github.com/resumeme");
        Long resumeId = 1L;

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(resumeService.updateReferenceLink(resume, request.toEntity())).willReturn(1L);

        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/resumes/{resumeId}/link", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/updateLink",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("linkType").type(STRING).description("링크 유형(깃허브 주소, 블로그 주소, 기타)"),
                                        fieldWithPath("url").type(STRING).description("링크 URL")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("업데이트된 이력서 아이디")
                                )

                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 참여한_이력서_리스트를_조회한다() throws Exception {
        Resume resume1 = new Resume("title1", mentee);
        Resume resume2 = new Resume("title2", mentee);
        setId(resume1, 1L);
        setId(resume2, 2L);

        given(resumeService.getAllByMenteeId(mentee.getId())).willReturn(List.of(resume1, resume2));

        ResultActions result = mvc.perform(get("/api/v1/resumes"));

        result.andExpect(status().isOk())
                .andDo(
                        document("resume/getOwn",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].id").type(NUMBER).description("이력서 id"),
                                        fieldWithPath("[].title").type(STRING).description("이력서 제목"),
                                        fieldWithPath("[].modifiedAt").type(NULL).description("수정 일자")
                                )
                        )
                );
    }

    @Test
    void 이력서와_관련된_이벤트_리스트_조회에_성공한다() throws Exception {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));

        Mentor mentorOne =  Mentor.builder()
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

        Mentor mentorTwo =  Mentor.builder()
                .id(2L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("mentor222@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김기안", "기안멘토", "01022632375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("유통회사 다님")
                .careerYear(5)
                .build();

        Event eventOne = new Event(openEvent, eventTimeInfo, mentorOne, List.of(BACK, FRONT));
        Event eventTwo = new Event(openEvent, eventTimeInfo, mentorTwo, List.of(DEVOPS));
        setId(eventOne,1L);
        setId(eventTwo,2L);

        MenteeToEvent menteeToEventOne = new MenteeToEvent(eventOne, 1L, 1L);
        MenteeToEvent menteeToEventTwo = new MenteeToEvent(eventTwo, 1L, 1L);

        given(menteeToEventService.getEventsRelatedToResume(any(Long.class))).willReturn(List.of(menteeToEventOne, menteeToEventTwo));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}/related-events", 1L));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/findEvents",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("resumeId").description("멘티가 작성한 이력서 아이디")
                                ),
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

    @Test
    void 이력서_삭제에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;

        willDoNothing().given(resumeService).delete(resumeId);

        // when
        ResultActions result = mvc.perform(delete("/api/v1/resumes/{resumeId}", resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                       document("resume/delete",
                               getDocumentRequest(),
                               getDocumentResponse(),
                               pathParameters(
                                       parameterWithName("resumeId").description("이력서 아이디")
                               )
                       )
                );
    }

}
