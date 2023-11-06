package org.devcourse.resumeme.business.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.admin.dto.MentorApplicationResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/applications")
public class MentorApplicationController {

    private final MentorService mentorService;

    private final MentorApplicationService mentorApplicationService;

    @GetMapping
    public List<MentorApplicationResponse> getAll() {
        return mentorApplicationService.getAll().stream()
                .map(mentorApplication -> new MentorApplicationResponse(mentorApplication.getId(), mentorApplication.mentorName()))
                .toList();
    }

    @DeleteMapping("/{applicationId}/{type}")
    public void processApplication(@PathVariable Long applicationId, @PathVariable String type) {
        Long mentorId = mentorApplicationService.delete(applicationId);

        mentorService.updateRole(mentorId, ApplicationProcessType.valueOf(type.toUpperCase()));
    }

}
