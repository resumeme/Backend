package org.devcourse.resumeme.business.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.entity.Snapshot;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.activityCreateRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SnapshotControllerTest extends ControllerUnitTest {

    @Test
    void 스냅샷_조회에_성공한다() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ActivityCreateRequest request = activityCreateRequest();
        Component component = request.toEntity().of(1L);
        Activity activity = new Activity(Converter.from(component));
        ActivityResponse response = new ActivityResponse("activities", activity, component);

        List<ComponentResponse> response1 = List.of(response);
        Map<String, List<ComponentResponse>> activities = new HashMap<>();
        activities.put("activities", response1);
        String s = objectMapper.writeValueAsString(activities);
        given(snapshotService.getByResumeId(1L)).willReturn(new Snapshot(s, 1L));

        // when
        ResultActions result = mvc.perform(get("/api/v1/snapshot?resumeId=" + 1L));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/snapshot",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                queryParameters(
                                        parameterWithName("resumeId").description("원본 이력서 아이디")
                                )
                        )
                );
    }

}
