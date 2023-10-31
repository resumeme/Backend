package org.devcourse.resumeme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.CreateResultRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.result.ResultNotice;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.user.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResultNoticeControllerTest extends ControllerUnitTest {

    @Test
    void 합격_자소서_소개글을_작성한다() throws Exception {
        // given
        Mentee mentee = Mentee.builder()
                .id(1L)
                .email("email")
                .provider(Provider.KAKAO)
                .interestedPositions(Set.of("FRONT", "BACK"))
                .interestedFields(Set.of("RETAIL"))
                .build();
        CreateResultRequest request = new CreateResultRequest(1L, "content");
        given(resumeService.getOne(request.resumeId())).willReturn(new Resume("title", mentee));
        given(resultService.create(any(ResultNotice.class))).willReturn(1L);

        // when
        ResultActions result = mvc.perform(post("/api/v1/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("result/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("resumeId").type(NUMBER).description("소개 할 합격 이력서 아이디"),
                                        fieldWithPath("content").type(STRING).description("작성 된 소개 글")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성 된 소개 글 아이디")
                                )
                        )
                );
    }

}
