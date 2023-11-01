package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.CareerCreateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Duty;
import org.devcourse.resumeme.domain.resume.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CareerControllerTest extends ControllerUnitTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 업무경험_저장에_성공한다() throws Exception {
        CareerCreateRequest request = new CareerCreateRequest("company name", "BACK", List.of("java", "spring"), List.of(new CareerCreateRequest.DutyRequest("title", "description", LocalDate.now(), LocalDate.now().plusYears(1L))), false, LocalDate.now(), LocalDate.now().plusYears(1L), "content");
        Long resumeId = 1L;
        Career career = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(careerService.create(career)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/careers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("career/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("companyName").type(STRING).description("회사명"),
                                        fieldWithPath("position").type(STRING).description("포지션"),
                                        fieldWithPath("skills[]").type(ARRAY).description("List of skills"),
                                        fieldWithPath("duties[].title").type(STRING).description("제목"),
                                        fieldWithPath("duties[].description").type(STRING).description("설명"),
                                        fieldWithPath("duties[].startDate").type(STRING).description("시작일"),
                                        fieldWithPath("duties[].endDate").type(STRING).description("종료일"),
                                        fieldWithPath("isCurrentlyEmployed").type(BOOLEAN).description("현재 근무 여부"),
                                        fieldWithPath("careerStartDate").type(STRING).description("경력 시작일"),
                                        fieldWithPath("endDate").type(STRING).description("종료일"),
                                        fieldWithPath("careerContent").type(STRING).description("경력 내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 경력 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 업무경험_조회에_성공한다() throws Exception {
        Long careerId = 1L;
        Career career = new Career("company name", "BACK", resume, List.of("java", "spring"), List.of(new Duty("title", LocalDate.now(), LocalDate.now().plusYears(1L), "description")), false, LocalDate.now(), LocalDate.now().plusYears(1L), "content");

        given(careerService.getOne(careerId)).willReturn(career);

        ResultActions result = mvc.perform(get("/api/v1/resume/{careerId}", careerId));


        result
                .andExpect(status().isOk())
                .andDo(
                        document("career/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("companyName").type(STRING).description("회사명"),
                                        fieldWithPath("position").type(STRING).description("직책"),
                                        fieldWithPath("skills").type(ARRAY).description("기술 목록"),
                                        fieldWithPath("duties[].title").type(STRING).description("업무 제목"),
                                        fieldWithPath("duties[].description").type(STRING).description("업무 설명"),
                                        fieldWithPath("duties[].startDate").type(STRING).description("업무 시작일"),
                                        fieldWithPath("duties[].endDate").type(STRING).description("업무 종료일"),
                                        fieldWithPath("isCurrentlyEmployed").type(BOOLEAN).description("현재 재직 상태"),
                                        fieldWithPath("careerStartDate").type(STRING).description("경력 시작일"),
                                        fieldWithPath("endDate").type(STRING).description("경력 종료일"),
                                        fieldWithPath("careerContent").type(STRING).description("경력 상세 내용")
                                )
                        ));

    }

}
