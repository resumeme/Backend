package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.controller.dto.EventRejectRequest;
import org.devcourse.resumeme.controller.dto.EventResponse;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.event.MenteeToEvent;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.global.auth.model.JwtUser;
import org.devcourse.resumeme.service.EventService;
import org.devcourse.resumeme.service.MentorService;
import org.devcourse.resumeme.service.ResumeService;
import org.devcourse.resumeme.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.service.vo.EventReject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    private final ResumeService resumeService;

    private final MentorService mentorService;

    @PostMapping
    public IdResponse createEvent(@RequestBody EventCreateRequest request, @AuthenticationPrincipal JwtUser user) {
        /* 인증 유저 아이디를 통한 멘토 찾아오기 */
        Mentor mentor = mentorService.getOne(user.id());
        Event event = request.toEntity(mentor);

        return new IdResponse(eventService.create(event));
    }

    @PatchMapping("/{eventId}")
    public IdResponse applyEvent(@PathVariable Long eventId, @RequestBody ApplyToEventRequest request, @AuthenticationPrincipal JwtUser user) {
        Long copyResumeId = resumeService.copyResume(request.resumeId());
        Event event = eventService.acceptMentee(new AcceptMenteeToEvent(eventId, copyResumeId, user.id()));

        return new IdResponse(eventService.getApplicantId(event, user.id()));
    }

    @PatchMapping("/{eventId}/mentee/{menteeId}")
    public void rejectApply(@PathVariable Long eventId, @PathVariable Long menteeId, @RequestBody EventRejectRequest request) {
        eventService.reject(new EventReject(eventId, menteeId, request.rejectMessage()));
    }

    @PatchMapping("/{eventId}/resume/{resumeId}/mentee")
    public void requestReview(@PathVariable Long eventId, @AuthenticationPrincipal JwtUser user) {
        eventService.requestReview(eventId, user.id());
    }

    @GetMapping("/{eventId}")
    public EventResponse getAllAttendResumes(@PathVariable Long eventId) {
        Event event = eventService.getOne(eventId);
        List<Resume> resumes = getResumes(event);

        return new EventResponse(event, resumes);
    }

    private List<Resume> getResumes(Event event) {
        List<MenteeToEvent> applicants = event.getApplicants();
        List<Long> menteeIds = applicants.stream()
                .map(MenteeToEvent::getMenteeId)
                .toList();
        List<Long> resumeIds = applicants.stream()
                .map(MenteeToEvent::getResumeId)
                .toList();

        return resumeService.getAll(resumeIds)
                .stream()
                .filter(resume -> menteeIds.contains(resume.menteeId()))
                .toList();
    }

}
