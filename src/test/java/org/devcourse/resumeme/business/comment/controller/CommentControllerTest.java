package org.devcourse.resumeme.business.comment.controller;

import org.devcourse.resumeme.business.comment.controller.dto.CommentCreateRequest;
import org.devcourse.resumeme.business.comment.controller.dto.CommentUpdateRequest;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.entity.Resume;
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
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.business.resume.domain.Property.CAREERS;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.COMMENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.COMPONENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EVENT_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_FOUND_DATA;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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

class CommentControllerTest extends ControllerUnitTest {

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
    void 리뷰_생성에_성공한다() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest(1L, "이력서가 맘에 안들어요");
        Long resumeId = 1L;
        Component component = new Component(CAREERS, "career", null, null, resumeId, List.of());
        Resume resume = new Resume("titlem", 1L);

        Comment comment = request.toEntity(resume);
        setId(comment, 1L);
        setId(component, 1L);
        Field lastModifiedAt = comment.getClass().getSuperclass().getDeclaredField("lastModifiedDate");
        lastModifiedAt.setAccessible(true);
        lastModifiedAt.set(comment, LocalDateTime.of(2023, 11, 15, 18, 0));
        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(reviewService.create(any(Comment.class), any(Long.class))).willReturn(comment);
        given(componentService.getOne(request.componentId())).willReturn(component);

        // when
        ResultActions result = mvc.perform(post("/api/v1/events/{eventId}/resumes/{resumeId}/comments", 1L, resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("comment/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name(), COMPONENT_NOT_FOUND.name(), NOT_FOUND_DATA.name())),
                                requestFields(
                                        fieldWithPath("componentId").type(NUMBER).description("이력서 component 아이디"),
                                        fieldWithPath("content").type(STRING).description("리뷰 내용")
                                ),
                                responseFields(
                                        fieldWithPath("commentId").type(NUMBER).description("첨삭 아이디"),
                                        fieldWithPath("componentId").type(NUMBER).description("이력서 component 아이디"),
                                        fieldWithPath("content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("lastModifiedAt").type(STRING).description("리뷰 내용")
                                )
                        )
                );
    }

    @Test
    void 이력서에_작성된_전체_리뷰를_조회한다() throws Exception {
        // given
        long eventId = 1L;
        long resumeId = 1L;

        Component component = new Component(CAREERS, "career", null, null, resumeId, List.of());

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        event.acceptMentee(1L, 1L);

        given(eventService.getOne(eventId)).willReturn(event);
        Comment comment = new Comment("리뷰 내용내용", 1L, new Resume("title", 1L));
        setId(comment, 1L);
        setId(component, 1L);
        Field lastModifiedAt = comment.getClass().getSuperclass().getDeclaredField("lastModifiedDate");
        lastModifiedAt.setAccessible(true);
        lastModifiedAt.set(comment, LocalDateTime.of(2023, 11, 15, 18, 0));

        given(eventService.getOverallReview(event, resumeId)).willReturn("좋은 이력서에요.");
        given(reviewService.getAllWithResumeId(resumeId)).willReturn(List.of(comment));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events/{eventId}/resumes/{resumeId}/comments", eventId, resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("comment/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(EVENT_NOT_FOUND.name(), RESUME_NOT_FOUND.name(), NOT_FOUND_DATA.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("참여한 이벤트 아이디"),
                                        parameterWithName("resumeId").description("이벤트에 참여 시 사용한 이력서 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("commentResponses[].commentId").type(NUMBER).description("리뷰 아이디"),
                                        fieldWithPath("commentResponses[].content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("commentResponses[].componentId").type(NUMBER).description("속해있는 블럭 아이디"),
                                        fieldWithPath("commentResponses[].lastModifiedAt").type(STRING).description("마지막 수정 시간"),
                                        fieldWithPath("overallReview").type(STRING).description("총평"),
                                        fieldWithPath("mentorId").type(NUMBER).description("멘토 아이디")
                                )


                        )
                );
    }

    @Test
    void 리뷰를_수정한다() throws Exception {
        // given
        long eventId = 1L;
        long resumeId = 1L;
        long commentId = 1L;

        CommentUpdateRequest request = new CommentUpdateRequest("새로운 첨삭 내용");

        // when
        ResultActions result = mvc.perform(patch("/api/v1/events/{eventId}/resumes/{resumeId}/comments/{commentId}", eventId, resumeId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("comment/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(COMMENT_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("참여한 이벤트 아이디"),
                                        parameterWithName("resumeId").description("이벤트에 참여 시 사용한 이력서 아이디"),
                                        parameterWithName("commentId").description("작성된 첨삭 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(STRING).description("새로운 첨삭 내용")
                                )

                        )
                );
    }

    @Test
    void 리뷰를_삭제한다() throws Exception {
        // given
        long eventId = 1L;
        long resumeId = 1L;
        long commentId = 1L;

        // when
        ResultActions result = mvc.perform(delete("/api/v1/events/{eventId}/resumes/{resumeId}/comments/{commentId}", eventId, resumeId, commentId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("comment/delete",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(COMMENT_NOT_FOUND.name(), RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("eventId").description("참여한 이벤트 아이디"),
                                        parameterWithName("resumeId").description("이벤트에 참여 시 사용한 이력서 아이디"),
                                        parameterWithName("commentId").description("작성된 첨삭 아이디")
                                )
                        )
                );
    }

}
