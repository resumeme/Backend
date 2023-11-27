package org.devcourse.resumeme.business.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.devcourse.resumeme.business.comment.controller.dto.CommentResponse;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.entity.Snapshot;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.activityCreateRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_FOUND_SNAPSHOT;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SnapshotControllerTest extends ControllerUnitTest {

    @Test
    void 이력서_스냅샷_조회에_성공한다() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ActivityCreateRequest request = activityCreateRequest();
        Component component = request.toEntity().of(1L);
        Activity activity = new Activity(Converter.from(component));
        ActivityResponse response = new ActivityResponse("activities", component);

        List<ComponentResponse> response1 = List.of(response);
        Map<String, List<ComponentResponse>> activities = new HashMap<>();
        activities.put("activities", response1);
        String s = objectMapper.writeValueAsString(activities);
        given(snapshotService.getByResumeId(1L)).willReturn(new Snapshot(s, null, 1L));

        // when
        ResultActions result = mvc.perform(get("/api/v1/snapshot?resumeId=" + 1L + "&type=resume"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/snapshot/resume",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(NOT_FOUND_SNAPSHOT.name())),
                                queryParameters(
                                        parameterWithName("resumeId").description("원본 이력서 아이디"),
                                        parameterWithName("type").description("이력서 지정")
                                )
                        )
                );
    }

    @Test
    void 피드백_스냅샷_조회에_성공한다() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<CommentResponse> comments = List.of(new CommentResponse(1L, "content", 1L, LocalDateTime.now()));
        CommentWithReviewResponse response = new CommentWithReviewResponse(comments, "총평", 1L);
        String s = objectMapper.writeValueAsString(response);

        given(snapshotService.getByResumeId(1L)).willReturn(new Snapshot(null, s, 1L));

        // when
        ResultActions result = mvc.perform(get("/api/v1/snapshot?resumeId=" + 1L + "&type=comment"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("resume/snapshot/comment",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                exceptionResponse(List.of(NOT_FOUND_SNAPSHOT.name())),
                                queryParameters(
                                        parameterWithName("resumeId").description("원본 이력서 아이디"),
                                        parameterWithName("type").description("피드백 지정")
                                )
                        )
                );
    }

}
