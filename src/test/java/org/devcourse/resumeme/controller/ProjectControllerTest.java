package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.ProjectCreateDto;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Project;
import org.devcourse.resumeme.domain.resume.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerUnitTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 프로젝트_저장에_성공한다() throws Exception {
        ProjectCreateDto request = new ProjectCreateDto("프로젝트", 2023L, true, "member1, member2, member3", List.of("java", "Spring"), "content", "https://example.com");
        Long resumeId = 1L;
        Project project = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(projectService.create(project)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("project/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("projectName").type(STRING).description("프로젝트명"),
                                        fieldWithPath("productionYear").type(NUMBER).description("생산 연도"),
                                        fieldWithPath("isTeam").type(BOOLEAN).description("팀 프로젝트 여부"),
                                        fieldWithPath("teamMembers").type(STRING).description("팀원 목록"),
                                        fieldWithPath("skills[]").type(ARRAY).description("기술 목록"),
                                        fieldWithPath("projectContent").type(STRING).description("프로젝트 내용"),
                                        fieldWithPath("projectUrl").type(STRING).description("프로젝트 URL")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 프로젝트 ID")
                                )
                        )
                );
    }

}
