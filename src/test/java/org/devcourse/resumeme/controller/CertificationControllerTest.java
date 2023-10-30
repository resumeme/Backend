package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.CertificationCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Certification;
import org.devcourse.resumeme.domain.resume.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CertificationControllerTest extends ControllerUnitTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 인증서_저장에_성공한다() throws Exception {
        CertificationCreateRequest request = new CertificationCreateRequest("인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
        Long resumeId = 1L;
        Certification certification = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(certificationService.create(certification)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/certifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("certification/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("certificationTitle").type(STRING).description("인증서명"),
                                        fieldWithPath("acquisitionDate").type(STRING).description("취득일"),
                                        fieldWithPath("issuingAuthority").type(STRING).description("발급기관"),
                                        fieldWithPath("link").type(STRING).description("링크"),
                                        fieldWithPath("description").type(STRING).description("설명")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 인증서 ID")
                                )
                        )
                );
    }
}
