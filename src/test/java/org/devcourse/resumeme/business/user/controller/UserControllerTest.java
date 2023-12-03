package org.devcourse.resumeme.business.user.controller;

import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.vo.UserInfoVo;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.INTEGER;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTOR_NOT_FOUND;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerUnitTest {

    private Mentor mentor;

    private Mentee mentee;

    @BeforeEach
    void setUp() {
        mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy4567@naver.com")
                .requiredInfo(new RequiredInfo("김건재", "gunjay", "01058337248", Role.ROLE_MENTEE))
                .interestedPositions(Set.of("FRONT", "BACK"))
                .interestedFields(Set.of("COMMERCE"))
                .introduce("빨리 취업하고싶어요! 화이팅~!")
                .build();
    }

    @Test
    @WithMockCustomUser(role = "ROLE_MENTOR")
    void 멘토는_마이페이지_정보조회에_성공한다() throws Exception {
        // given
        given(userService.getOne(Role.ROLE_MENTOR, 1L)).willReturn(new UserInfoVo(mentor));

        // when
        ResultActions result = mvc.perform(get("/api/v1/user"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user/mentor/findMy",
                                exceptionResponse(
                                        List.of(MENTEE_NOT_FOUND.name())
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("멘토 id"),
                                        fieldWithPath("imageUrl").type(STRING).description("프로필 이미지"),
                                        fieldWithPath("realName").type(STRING).description("실명"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("role").type(STRING).description("역할").attributes(constraints("mentor")),
                                        fieldWithPath("experiencedPositions").type(ARRAY).description("활동 직무"),
                                        fieldWithPath("interestedPositions").type(NULL).description("관심 직무"),
                                        fieldWithPath("interestedFields").type(NULL).description("관심 분야"),
                                        fieldWithPath("careerContent").type(STRING).description("경력 사항"),
                                        fieldWithPath("careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    void 멘티는_마이페이지_정보조회에_성공한다() throws Exception {
        // given
        given(userService.getOne(Role.ROLE_MENTEE, 1L)).willReturn(new UserInfoVo(mentee));

        // when
        ResultActions result = mvc.perform(get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("user/mentee/findMy",
                                getDocumentRequest(),
                                exceptionResponse(
                                        List.of(MENTOR_NOT_FOUND.name())
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("멘티 id"),
                                        fieldWithPath("imageUrl").type(STRING).description("프로필 이미지"),
                                        fieldWithPath("realName").type(STRING).description("실명"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                                        fieldWithPath("role").type(STRING).description("역할").attributes(constraints("mentee")),
                                        fieldWithPath("experiencedPositions").type(NULL).description("활동 직무"),
                                        fieldWithPath("interestedPositions").type(ARRAY).description("관심 직무").optional(),
                                        fieldWithPath("interestedFields").type(ARRAY).description("관심 분야").optional(),
                                        fieldWithPath("careerContent").type(NULL).description("경력 사항"),
                                        fieldWithPath("careerYear").type(INTEGER).description("경력 연차"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개").optional()
                                )
                        )
                );

    }

}
