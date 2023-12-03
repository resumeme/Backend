package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.resume.ResumeRequest;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.entity.ResumeInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
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

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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

        resume = new Resume("title", 1L);
    }

    @Test
    @WithMockCustomUser
    void 이력서_생성에_성공한다() throws Exception {
        ResumeRequest request = new ResumeRequest("title");
        Resume resume = request.toEntity(1L);

        given(userService.getOne(any())).willReturn(mentee.from());
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
                                exceptionResponse(List.of(MENTEE_NOT_FOUND.name(), NO_EMPTY_VALUE.name())),
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
                .menteeId(1L)
                .build();
        Field field = resume.getClass().getDeclaredField("originResumeId");
        field.setAccessible(true);
        field.set(resume, 2L);
        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(userInfoProvider.getOne(1L)).willReturn(new UserResponse(1L, "nickname", "name", "email", "01012345678", "url"));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resumes/{resumeId}/basic", resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/basicInfo",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                                pathParameters(
                                        parameterWithName("resumeId").description("이력서 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("title").type(STRING).description("이력서 제목"),
                                        fieldWithPath("position").type(STRING).description("지원 분야"),
                                        fieldWithPath("skills").type(ARRAY).description("사용 기술"),
                                        fieldWithPath("introduce").type(STRING).description("자기소개글"),
                                        fieldWithPath("originResumeId").type(NUMBER).description("원본 복사 전 이력서 아이디 (NULL 가능)"),
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
    void 참여한_이력서_리스트를_조회한다() throws Exception {
        Resume resume1 = new Resume("title1", 1L);
        Resume resume2 = new Resume("title2", 2L);
        setId(resume1, 1L);
        setId(resume2, 2L);
        setModifiedAt(resume1, LocalDateTime.now());
        setModifiedAt(resume2, LocalDateTime.now());
        setPosition(resume1, "FRONT");
        setPosition(resume2, "BACK");
        setMemo(resume1, "memo");
        setMemo(resume2, "memo");

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
                                        fieldWithPath("[].modifiedAt").type(STRING).description("수정 일자"),
                                        fieldWithPath("[].position").type(STRING).description("직무"),
                                        fieldWithPath("[].memo").type(STRING).description("메모")
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
                               exceptionResponse(List.of(RESUME_NOT_FOUND.name())),
                               pathParameters(
                                       parameterWithName("resumeId").description("이력서 아이디")
                               )
                       )
                );
    }

    private void setModifiedAt(Object target, LocalDateTime localDateTime) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getSuperclass().getDeclaredField("lastModifiedDate");
        field.setAccessible(true);
        field.set(target, localDateTime);
    }

    private void setPosition(Resume resume, String position) throws NoSuchFieldException, IllegalAccessException {
        Field field = resume.getResumeInfo().getClass().getDeclaredField("position");
        field.setAccessible(true);
        field.set(resume.getResumeInfo(), position);
    }

    private void setMemo(Object target, String memo) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField("memo");
        field.setAccessible(true);
        field.set(target, memo);
    }

}
