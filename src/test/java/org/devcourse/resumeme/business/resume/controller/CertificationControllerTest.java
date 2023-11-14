package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.Certification;
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

import static org.devcourse.resumeme.business.resume.domain.BlockType.CERTIFICATION;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CertificationControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("image.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("devcourse@naver.com")
                .refreshToken("fjiejrwoosdfsfsddfss")
                .requiredInfo(new RequiredInfo("이동호", "동동", "01022283833", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
    }

    @Test
    void 인증서_저장에_성공한다() throws Exception {
        CertificationCreateRequest request = new CertificationCreateRequest("인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
        Long resumeId = 1L;
        Certification certification = request.toEntity();
        Component component = certification.of(resumeId);

        given(componentService.create(component, BlockType.CAREER)).willReturn(1L);


        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/certifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/certification/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("certificationTitle").type(STRING).description("인증서명"),
                                        fieldWithPath("acquisitionDate").type(STRING).description("취득일").optional(),
                                        fieldWithPath("issuingAuthority").type(STRING).description("발급기관").optional(),
                                        fieldWithPath("link").type(STRING).description("링크").optional(),
                                        fieldWithPath("description").type(STRING).description("설명").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 인증서 ID")
                                )
                        )
                );
    }

    @Test
    void 인증서_수정에_성공한다() throws Exception {
        CertificationCreateRequest request = new CertificationCreateRequest("인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
        Long resumeId = 1L;
        Long componentId = 1L;

        Certification certification = request.toEntity();
        Component component = certification.of(resumeId);

        given(componentService.delete(componentId)).willReturn("certifications");
        given(componentService.create(component, BlockType.CAREER)).willReturn(1L);

        ResultActions result = mvc.perform(patch("/api/v1/resume/" + resumeId + "/certifications/components/{componentId}", componentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/certification/update",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                                        fieldWithPath("certificationTitle").type(STRING).description("인증서명"),
                                        fieldWithPath("acquisitionDate").type(STRING).description("취득일").optional(),
                                        fieldWithPath("issuingAuthority").type(STRING).description("발급기관").optional(),
                                        fieldWithPath("link").type(STRING).description("링크").optional(),
                                        fieldWithPath("description").type(STRING).description("설명").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("수정된 인증서 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 인증서_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        Certification certification = new Certification("인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
        Component component = certification.of(resumeId);

        Component certification1 = new Component(CERTIFICATION.getUrlParameter(), null, null, null, resumeId, List.of(component));
        setId(component, 1L);
        given(componentService.getAll(resumeId)).willReturn(List.of(certification1));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/certifications"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/certification/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                                        fieldWithPath("[].certificationTitle").type(STRING).description("자격증 제목"),
                                        fieldWithPath("[].acquisitionDate").type(STRING).description("취득 일자"),
                                        fieldWithPath("[].issuingAuthority").type(STRING).description("발급 기관"),
                                        fieldWithPath("[].link").type(STRING).description("링크"),
                                        fieldWithPath("[].description").type(STRING).description("설명")
                                )
                        ));

    }
}
