package org.devcourse.resumeme.business.snapshot.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.snapshot.controller.dto.SnapshotResponse;
import org.devcourse.resumeme.business.snapshot.entity.SnapshotType;
import org.devcourse.resumeme.business.snapshot.service.SnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/snapshot")
public class SnapshotController {

    private final SnapshotService snapshotService;

    @GetMapping
    public SnapshotResponse getSnapshot(@RequestParam Long resumeId, @RequestParam SnapshotType type) {
        return snapshotService.getByResumeId(resumeId, type).toResponse();
    }

}
