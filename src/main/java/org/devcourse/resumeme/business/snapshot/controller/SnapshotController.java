package org.devcourse.resumeme.business.snapshot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.CommentSnapshotResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.ResumeSnapshotResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.SnapshotResponse;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.service.SnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/snapshot")
public class SnapshotController {

    private final SnapshotService snapshotService;

    @GetMapping
    public SnapshotResponse getSnapshot(@RequestParam Long resumeId, @RequestParam String type) throws IOException {
        Snapshot snapshot = snapshotService.getByResumeId(resumeId);

        String data = snapshot.get(type);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (type.equals("resume")) {
            AllComponentResponse response = objectMapper.readValue(data, new TypeReference<>() {});

            return new ResumeSnapshotResponse(response);
        }

        CommentWithReviewResponse response = objectMapper.readValue(data, new TypeReference<>() {});

        return new CommentSnapshotResponse(response);
    }

}
