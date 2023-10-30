package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ApplicationProcessType;
import org.devcourse.resumeme.service.MentorApplicationService;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/applications")
public class MentorApplicationController {

    private final MentorService mentorService;

    private final MentorApplicationService mentorApplicationService;

    @DeleteMapping("/{applicationId}/{type}")
    public void processApplication(@PathVariable Long applicationId, @PathVariable String type) {
        Long mentorId = mentorApplicationService.delete(applicationId);

        mentorService.updateRole(mentorId, ApplicationProcessType.valueOf(type.toUpperCase()));
    }

}
