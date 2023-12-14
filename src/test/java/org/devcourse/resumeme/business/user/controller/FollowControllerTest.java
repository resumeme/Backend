package org.devcourse.resumeme.business.user.controller;

import org.devcourse.resumeme.business.user.controller.dto.FollowRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.devcourse.resumeme.common.util.DocumentLinkGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ALREADY_FOLLOWING;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EXCEEDED_FOLLOW_MAX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
        Mentor mentorOne =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("progrers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .introduce("반가워요")
                .build();

        Mentor mentorThree =  Mentor.builder()
                .id(3L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("progs3rs33@gmail.com")
                .refreshToken("dddfeggegeeegegee")
                .requiredInfo(new RequiredInfo("이기안", "기안멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("DEVOPS"))
                .careerContent("제조업 종사자")
                .careerYear(5)
                .introduce("프로첨삭러")
                .build();

        Follow followMentorOne = new Follow(1L, 1L);
        Follow followMentorThree = new Follow(1L, 3L);
        setId(followMentorOne, 1L);
        setId(followMentorThree, 2L);

        given(followService.getFollowings(any(Long.class))).willReturn(List.of(followMentorOne, followMentorThree));
        given(userService.getByIds(any())).willReturn(List.of(mentorOne.from(), mentorThree.from()));

        // when
        ResultActions result = mvc.perform(get("/api/v1/follows"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("follows/getAll",
                                responseFields(
                                        fieldWithPath("[0].followId").type(LONG).description("팔로우 아이디"),
                                        fieldWithPath("[0].mentorInfo").type(OBJECT).description("팔로우 하는 멘토 정보"),
                                        fieldWithPath("[0].mentorInfo.id").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("[0].mentorInfo.imageUrl").type(STRING).description("멘토 프로필 이미지"),
                                        fieldWithPath("[0].mentorInfo.nickname").type(STRING).description("멘토 닉네임"),
                                        fieldWithPath("[0].mentorInfo.experiencedPositions").type(ARRAY).description("팔로우 하는 멘토 아이디").description("활동 직무").description(generateLinkCode(DocumentLinkGenerator.DocUrl.POSITION)),
                                        fieldWithPath("[0].mentorInfo.careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("[0].mentorInfo.careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("[0].mentorInfo.introduce").type(STRING).description("자기소개").optional(),
                                        fieldWithPath("[1].followId").type(LONG).description("팔로우 아이디"),
                                        fieldWithPath("[1].mentorInfo").type(OBJECT).description("팔로우 하는 멘토 정보"),
                                        fieldWithPath("[1].mentorInfo.id").type(NUMBER).description("멘토 아이디"),
                                        fieldWithPath("[1].mentorInfo.imageUrl").type(STRING).description("멘토 프로필 이미지"),
                                        fieldWithPath("[1].mentorInfo.nickname").type(STRING).description("멘토 닉네임"),
                                        fieldWithPath("[1].mentorInfo.experiencedPositions").type(ARRAY).description("팔로우 하는 멘토 아이디").description("활동 직무").description(generateLinkCode(DocumentLinkGenerator.DocUrl.POSITION)),
                                        fieldWithPath("[1].mentorInfo.careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("[1].mentorInfo.careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("[1].mentorInfo.introduce").type(STRING).description("자기소개").optional()

                                )
                        )
                );

    }

    @Test
    @WithMockCustomUser
    void 멘티는_멘토_아이디로_팔로우_여부를_조회할_수_있다() throws Exception {
        // given
        Optional<Follow> follow = Optional.of(new Follow(1L, 1L));
        setId(follow.get(), 1L);

        given(followService.getFollow(any(Long.class), any(Long.class))).willReturn(any(Long.class));

        // when
        ResultActions result = mvc.perform(get("/api/v1/follows/mentors/{mentorId}", 1L));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("follows/find",
                                getDocumentRequest(),
                                pathParameters(
                                        parameterWithName("mentorId").description("팔로우 여부 조회할 멘토 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("팔로우 아이디").attributes(constraints("팔로우 상태가 아닐경우 null"))
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
