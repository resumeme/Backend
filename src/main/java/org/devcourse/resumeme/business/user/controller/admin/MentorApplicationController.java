package org.devcourse.resumeme.business.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.mail.service.EmailService;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.admin.dto.MentorApplicationResponse;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.devcourse.resumeme.business.mail.service.EmailInfoGenerator.createMentorApprovalMail;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/applications")
public class MentorApplicationController {

    private final MentorService mentorService;

    private final EmailService emailService;

    private final MentorApplicationService mentorApplicationService;

    @GetMapping
    public String getAll(Model model) {
        List<MentorApplicationResponse> mentorApplications = mentorApplicationService.getAll().stream()
                .map(mentorApplication -> new MentorApplicationResponse(mentorApplication.getId(), mentorApplication.mentorName()))
                .toList();
        model.addAttribute("mentorApplications", mentorApplications);
        return "mentorApprovalPage";
    }

    @PostMapping("/{applicationId}/approve")
    public String approveApplication(@PathVariable Long applicationId) {
        processApplication(applicationId, ApplicationProcessType.ACCEPT.toString());
        return "redirect:/admin/applications";
    }

    @PostMapping("/{applicationId}/reject")
    public String rejectApplication(@PathVariable Long applicationId) {
        processApplication(applicationId, ApplicationProcessType.REJECT.toString());
        return "redirect:/admin/applications";
    }

    private void processApplication(Long applicationId, String type) {
        Long mentorId = mentorApplicationService.delete(applicationId);
        mentorService.updateRole(mentorId, ApplicationProcessType.valueOf(type.toUpperCase()));
        emailService.sendEmail(createMentorApprovalMail(mentorService.getOne(mentorId)));
    }

}
