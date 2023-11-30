package org.devcourse.resumeme.business.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.mail.service.EmailService;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.admin.dto.MentorApplicationResponse;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplication;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.devcourse.resumeme.business.mail.service.EmailInfoGenerator.createMentorApprovalMail;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/applications")
public class MentorApplicationController {

    private final UserService userService;

    private final EmailService emailService;

    private final MentorApplicationService mentorApplicationService;

    @GetMapping
    public String getAll(Model model) {
        List<MentorApplication> applications = mentorApplicationService.getAll();

        List<Long> mentorIds = applications.stream()
                .map(application -> application.getMentorId())
                .toList();
        Map<Long, Mentor> mentors = userService.getByIds(mentorIds).stream()
                .map(User::toMentor)
                .collect(Collectors.toMap(Mentor::getId, Function.identity()));

        List<MentorApplicationResponse> mentorApplications = applications.stream()
                .map(mentorApplication -> new MentorApplicationResponse(mentorApplication.getId(), mentors.get(mentorApplication.getMentorId()).getNickname()))
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
        userService.updateRole(mentorId, ApplicationProcessType.valueOf(type.toUpperCase()));
        emailService.sendEmail(createMentorApprovalMail(userService.getOne(mentorId).toMentor()));
    }

}
