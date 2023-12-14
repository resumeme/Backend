package org.devcourse.resumeme.business.event.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.v2.ApplyUpdateRequest;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.ApplyUpdateVo;
import org.devcourse.resumeme.business.resume.service.ResumeProvider;
import org.devcourse.resumeme.business.snapshot.service.SnapshotCapture;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appliments/events")
public class MenteeToEventController {

    private final ResumeProvider resumeProvider;

    private final MenteeToEventService applyService;

    private final SnapshotCapture commentCapture;

    @GetMapping("/{eventId}")
    public IdResponse getParticipantRecord(@PathVariable Long eventId, @AuthenticationPrincipal JwtUser user) {
        Long menteeId = user.id();

        return new IdResponse(applyService.getRecord(eventId, menteeId));
    }
    
    @PostMapping("/{eventId}")
    public void applyEvent(@PathVariable Long eventId, @RequestBody ApplyToEventRequest request, @AuthenticationPrincipal JwtUser user) {
        Long copyResumeId = resumeProvider.copy(request.resumeId());
        applyService.acceptMentee(new AcceptMenteeToEvent(eventId, user.id(), copyResumeId));
    }

    @PatchMapping("/{eventId}")
    public void updateEvent(@PathVariable Long eventId, @RequestBody ApplyUpdateRequest request) {
        ApplyUpdateVo applyUpdateVo = request.toVo();
        Long resumeId = applyService.update(eventId, applyUpdateVo);

        commentCapture.capture(eventId, resumeId);
    }

}
