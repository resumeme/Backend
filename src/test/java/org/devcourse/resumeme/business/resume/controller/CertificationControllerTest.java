package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationCreateRequest;
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

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
                        document("resume/certification/create",
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

    @Test
    @WithMockUser
    void 업무경험_조회에_성공한다() throws Exception {
        Long resumeId = 1L;
        Certification certification = new Certification(resume, "인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
        Resume savedResume = resume.builder()
                .certification(List.of(certification))
                .build();

        given(resumeService.getOne(resumeId)).willReturn(savedResume);

        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/certifications"));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/certification/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].certificationTitle").type(STRING).description("자격증 제목"),
                                        fieldWithPath("[].acquisitionDate").type(STRING).description("취득 일자"),
                                        fieldWithPath("[].issuingAuthority").type(STRING).description("발급 기관"),
                                        fieldWithPath("[].link").type(STRING).description("링크"),
                                        fieldWithPath("[].description").type(STRING).description("설명")
                                )
                        ));

    }
}
