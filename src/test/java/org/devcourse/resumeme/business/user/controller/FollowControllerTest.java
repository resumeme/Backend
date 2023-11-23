package org.devcourse.resumeme.business.user.controller;

import org.devcourse.resumeme.business.user.controller.dto.FollowRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ALREADY_FOLLOWING;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EXCEEDED_FOLLOW_MAX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerTest extends ControllerUnitTest {

    @Test
    @WithMockCustomUser
    void 멘티는_첨삭_이벤트_오픈_알림을_받고싶은_멘토를_팔로우_할_수_있다() throws Exception {
        // given
        FollowRequest request = new FollowRequest(1L);
        given(followService.create(any(Follow.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/follows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("follows/create",
                                getDocumentRequest(),
                                exceptionResponse(
                                        List.of(EXCEEDED_FOLLOW_MAX.name(), ALREADY_FOLLOWING.name())
                                ),
                                requestFields(
                                        fieldWithPath("mentorId").type(NUMBER).description("멘토 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 팔로우 아이디")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티의_팔로잉_하는_멘토_리스트를_조회할_수_있다() throws Exception {
        // given
        Follow followMentorOne = new Follow(1L, 1L);
        Follow followMentorThree = new Follow(1L, 3L);
        Follow followMentorFour = new Follow(1L, 4L);
        Follow followMentorEleven = new Follow(1L, 11L);
        setId(followMentorOne, 1L);
        setId(followMentorThree, 2L);
        setId(followMentorFour, 4L);
        setId(followMentorEleven, 5L);

        given(followService.getFollowings(any(Long.class))).willReturn(List.of(followMentorOne, followMentorThree, followMentorFour, followMentorEleven));

        // when
        ResultActions result = mvc.perform(get("/api/v1/follows"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("follows/getAll",
                                responseFields(
//                                        fieldWithPath("[]").type(ARRAY).description("이벤트 아이디"),
                                        fieldWithPath("[0].followId").type(NUMBER).description("팔로우 아이디"),
                                        fieldWithPath("[0].mentorId").type(NUMBER).description("팔로우 하는 멘토 아이디"),
                                        fieldWithPath("[1].followId").type(NUMBER).description("팔로우 아이디"),
                                        fieldWithPath("[1].mentorId").type(NUMBER).description("팔로우 하는 멘토 아이디"),
                                        fieldWithPath("[2].followId").type(NUMBER).description("팔로우 아이디"),
                                        fieldWithPath("[2].mentorId").type(NUMBER).description("팔로우 하는 멘토 아이디"),
                                        fieldWithPath("[3].followId").type(NUMBER).description("팔로우 아이디"),
                                        fieldWithPath("[3].mentorId").type(NUMBER).description("팔로우 하는 멘토 아이디")

                                )
                        )
                );

    }

    @Test
    @WithMockCustomUser
    void 특정_멘토에_대해_팔로우를_취소할_수_있다() throws Exception {
        // given
        doNothing().when(followService).unfollow(any(Long.class));

        // when
        // when
        ResultActions result = mvc.perform(delete("/api/v1/follows/{followId}", 1L));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("follows/delete",
                                pathParameters(
                                        parameterWithName("followId").description("삭제하려는 팔로우 아이디")
                                )
                        )
                );
    }
}
