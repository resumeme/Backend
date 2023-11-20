package org.devcourse.resumeme.business.resume.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.entity.Snapshot;
import org.devcourse.resumeme.business.resume.service.SnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/snapshot")
public class SnapshotController {

    private final SnapshotService snapshotService;

    @GetMapping
    public Map<String, List<ComponentResponse>> getSnapshot(@RequestParam Long resumeId) throws IOException {
        Snapshot snapshot = snapshotService.getByResumeId(resumeId);

        String resumeData = snapshot.getResumeData();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, List<ComponentResponse>> response = objectMapper.readValue(resumeData, new TypeReference<>() {});

        return response;
    }

}
