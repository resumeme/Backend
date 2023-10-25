package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ApplyToEventRequest;
import org.devcourse.resumeme.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.service.EventService;
import org.devcourse.resumeme.service.vo.AcceptMenteeToEvent;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public IdResponse createEvent(@RequestBody EventCreateRequest request /* @AuthenticationPrincipal 인증 유저 */) {
        /* 인증 유저 아이디를 통한 멘토 찾아오기 */
        Mentor mentor = new Mentor();
        Event event = request.toEntity(mentor);

        return new IdResponse(eventService.create(event));
    }

    @PatchMapping("/{eventId}")
    public IdResponse applyEvent(@PathVariable Long eventId, @RequestBody ApplyToEventRequest request /* @AuthenticationPrincipal 인증 유저 */) {
        /* 인증 유저 아이디 -> 멘티 아이디 찾아오기 */
        Long menteeId = 1L;
        Event event = eventService.acceptMentee(new AcceptMenteeToEvent(eventId, request.resumeId(), menteeId));

        return new IdResponse(eventService.getApplicantId(event, menteeId));
    }

}
