package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectCreateRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.business.resume.domain.BlockType.PROJECT;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

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
    void 프로젝트_저장에_성공한다() throws Exception {
        // then
        ProjectCreateRequest request = new ProjectCreateRequest("프로젝트", 2023L, true, "member1, member2, member3", List.of("java", "Spring"), "content", "https://example.com");
        Long resumeId = 1L;
        Project project = request.toEntity();

        Component component = project.of(resumeId);

        given(componentService.create(component, PROJECT)).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/project/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("projectName").type(STRING).description("프로젝트명"),
                                        fieldWithPath("productionYear").type(NUMBER).description("생산 연도"),
                                        fieldWithPath("team").type(BOOLEAN).description("팀 프로젝트 여부").optional().attributes(constraints("true일 시 teamMembers 필수")),
                                        fieldWithPath("teamMembers").type(STRING).description("팀원 목록").optional(),
                                        fieldWithPath("skills[]").type(ARRAY).description("기술 목록").optional(),
                                        fieldWithPath("projectContent").type(STRING).description("프로젝트 내용").optional(),
                                        fieldWithPath("projectUrl").type(STRING).description("프로젝트 URL").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 프로젝트 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 업무경험_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        Project project = new Project("프로젝트", 2023L, "member1, member2, member3", List.of("java", "Spring"), "content", "https://example.com");
        Component component = project.of(resumeId);

        Component project1 = new Component(PROJECT.getUrlParameter(), null, null, null, resumeId, List.of(component));
        given(componentService.getAll(resumeId)).willReturn(List.of(project1));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/projects"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/project/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[]projectName").type(STRING).description("프로젝트명"),
                                        fieldWithPath("[]productionYear").type(NUMBER).description("제작 연도"),
                                        fieldWithPath("[]team").type(BOOLEAN).description("팀 프로젝트 여부"),
                                        fieldWithPath("[]teamMembers").type(STRING).optional().description("팀 구성원 (옵션)"),
                                        fieldWithPath("[]skills").type(ARRAY).description("기술 목록"),
                                        fieldWithPath("[]projectContent").type(STRING).description("프로젝트 내용"),
                                        fieldWithPath("[]projectUrl").type(STRING).description("프로젝트 URL")
                                )
                        ));

    }

}
