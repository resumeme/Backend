package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.DocumentLinkGenerator;
import org.devcourse.resumeme.controller.dto.ReviewCreateRequest;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.event.EventInfo;
import org.devcourse.resumeme.domain.event.EventTimeInfo;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.review.Review;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends ControllerUnitTest {

    @Test
    @WithMockCustomUser
    void 리뷰_생성에_성공한다() throws Exception {
        // given
        ReviewCreateRequest request = new ReviewCreateRequest("이력서가 맘에 안들어요", "ACTIVITY");
        Long resumeId = 1L;
        Mentee mentee = Mentee.builder()
                .id(1L)
                .email("email")
                .provider(Provider.KAKAO)
                .interestedPositions(Set.of("FRONT", "BACK"))
                .interestedFields(Set.of("RETAIL"))
                .build();
        Resume resume = new Resume("titlem", mentee);

        Review review = request.toEntity(resume);

        Field id = review.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(review, 1L);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(reviewService.create(any(Review.class))).willReturn(review);

        // when
        ResultActions result = mvc.perform(post("/api/v1/events/{eventId}/resume/{resumeId}/reviews", 1L, resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("review/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("blockType").type(STRING).description("이력서 각 블럭 타입")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("리뷰 아이디"),
                                        fieldWithPath("content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("blockType").type(STRING).description(generateLinkCode(BLOCK_TYPE))
                                )
                        )
                );
    }

    @Test
    void 이력서에_작성된_전체_리뷰를_조회한다() throws Exception {
        // given
        long eventId = 1L;
        long resumeId = 1L;
        Mentee mentee = Mentee.builder()
                .id(1L)
                .email("email")
                .provider(Provider.KAKAO)
                .interestedPositions(Set.of("FRONT", "BACK"))
                .interestedFields(Set.of("RETAIL"))
                .build();

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, new Mentor(), List.of());
        event.acceptMentee(1L, 1L);

        given(eventService.getOne(eventId)).willReturn(event);
        Review review = new Review("리뷰 내용내용", BlockType.CAREER, new Resume("title", mentee));
        Field field = review.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(review, 1L);

        given(reviewService.getAllWithResumeId(resumeId)).willReturn(List.of(review));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events/{eventId}/resume/{resumeId}/reviews", eventId, resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("review/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("참여한 이벤트 아이디"),
                                        parameterWithName("resumeId").description("이벤트에 참여 시 사용한 이력서 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(NUMBER).description("리뷰 아이디"),
                                        fieldWithPath("[].content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("[].blockType").type(STRING).description(generateLinkCode(BLOCK_TYPE))
                                )

                        )
                );
    }


}
