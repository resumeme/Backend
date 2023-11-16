package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.EventsResponse;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.resume.controller.dto.BasicResumeInfo;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeInfoRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeResponse;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.ResumeInfo;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    private final MenteeService menteeService;

    private final MenteeToEventService menteeToEventService;

    @PostMapping
    public IdResponse createResume(@AuthenticationPrincipal JwtUser user, @RequestBody ResumeRequest request) {
        Resume resume = request.toEntity(menteeService.getOne(user.id()));
        Long savedId = resumeService.create(resume);

        return new IdResponse(savedId);
    }

    @GetMapping("/{resumeId}/basic")
    public BasicResumeInfo getBasicInformation(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);

        return new BasicResumeInfo(resume);
    }
    
    @PatchMapping("/{resumeId}")
    public IdResponse updateResume(@PathVariable Long resumeId, @RequestBody ResumeInfoRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        ResumeInfo resumeInfo = request.toEntity();

        return new IdResponse(resumeService.updateResumeInfo(resume, resumeInfo));
    }

    @PatchMapping("/{resumeId}/title")
    public IdResponse updateResumeTitle(@PathVariable Long resumeId, @RequestBody ResumeRequest request) {
        Resume resume = resumeService.getOne(resumeId);

        return new IdResponse(resumeService.updateTitle(resume, request.title()));
    }

    @GetMapping
    public List<ResumeResponse> getAll(@AuthenticationPrincipal JwtUser user) {
        return resumeService.getAllByMenteeId(user.id()).stream()
                .map(ResumeResponse::new)
                .toList();
    }

    @GetMapping("/{resumeId}/related-events")
    public List<EventsResponse> getEvents(@PathVariable Long resumeId) {
        List<MenteeToEvent> relatedEvents = menteeToEventService.getEventsRelatedToResume(resumeId);

        return relatedEvents.stream()
                .map(menteeToEvent -> new EventsResponse(menteeToEvent.getEvent()))
                .toList();
    }

    @DeleteMapping("/{resumeId}")
    public void deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
    }

}
