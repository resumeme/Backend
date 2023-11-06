package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeInfoRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.RequestEntity.patch;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResumeControllerTest extends ControllerUnitTest {

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
                                        fieldWithPath("position").description("포지션"),
                                        fieldWithPath("skills").description("스킬 목록"),
                                        fieldWithPath("introduce").description("자기 소개")
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

}
