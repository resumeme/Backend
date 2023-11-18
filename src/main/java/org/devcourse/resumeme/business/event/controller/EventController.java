package org.devcourse.resumeme.business.event.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.CompleteEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventInfoResponse;
import org.devcourse.resumeme.business.event.controller.dto.EventRejectRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventResponse;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.EventReject;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
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

    private final EventPositionService eventPositionService;

    private final ComponentService componentService;

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
        Event event = eventService.acceptMentee(new AcceptMenteeToEvent(eventId, user.id(), copyResumeId));
        componentService.copy(request.resumeId(), copyResumeId);

        return new IdResponse(eventService.getApplicantId(eventId, user.id()));
    }

    @PatchMapping("/{eventId}/mentee/{menteeId}")
    public void rejectApply(@PathVariable Long eventId, @PathVariable Long menteeId, @RequestBody EventRejectRequest request) {
        eventService.reject(new EventReject(eventId, menteeId, request.rejectMessage()));
    }

    @PatchMapping("/{eventId}/resumes/{resumeId}/mentee")
    public void requestReview(@PathVariable Long eventId, @AuthenticationPrincipal JwtUser user) {
        eventService.requestReview(eventId, user.id());
    }

    @PatchMapping("/{eventId}/resumes/{resumeId}/complete")
    public void completeReview(
            @PathVariable Long eventId, @PathVariable Long resumeId, @RequestBody CompleteEventRequest request, @AuthenticationPrincipal JwtUser user
    ) {
        eventService.completeReview(eventId, request.comment(), resumeId);
    }

    @GetMapping("/{eventId}")
    public EventInfoResponse getAllAttendResumes(@PathVariable Long eventId) {
        Event event = eventService.getOne(eventId);
        List<EventPosition> positions = eventPositionService.getAll(eventId);

        return new EventInfoResponse(event, positions);
    }

    @GetMapping
    public List<EventResponse> getAll() {
        return eventService.getAll(null).stream()
                .map(event -> new EventResponse(event, eventPositionService.getAll(event.getId()), null))
                .toList();
    }

}
