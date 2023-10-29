package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.ReviewCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.review.Review;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        ResultActions result = mvc.perform(post("/api/v1/resume/{resumeId}/reviews",resumeId)
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
                                        fieldWithPath("blockType").type(STRING).description("리뷰한 블럭 타입")
                                )
                        )
                );
    }


}
