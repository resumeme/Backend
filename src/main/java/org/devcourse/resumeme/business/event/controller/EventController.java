package org.devcourse.resumeme.business.event.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.CompleteEventRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventCreateRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventInfoResponse;
import org.devcourse.resumeme.business.event.controller.dto.EventPageResponse;
import org.devcourse.resumeme.business.event.controller.dto.EventRejectRequest;
import org.devcourse.resumeme.business.event.controller.dto.EventResponse;
import org.devcourse.resumeme.business.event.controller.dto.EventUpdateRequest;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.event.service.vo.EventReject;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    private final MentorService mentorService;

    private final EventPositionService eventPositionService;

    @PostMapping
    public IdResponse createEvent(@RequestBody EventCreateRequest request, @AuthenticationPrincipal JwtUser user) {
        Mentor mentor = mentorService.getOne(user.id());
        Event event = request.toEntity(mentor);

        return new IdResponse(eventService.create(event));
    }

    @PatchMapping("/{eventId}")
    public IdResponse updateEvent(@RequestBody EventUpdateRequest request, @PathVariable Long eventId) {
        EventUpdateVo updateVo = request.toVo(eventId);
        eventService.update(updateVo);

        return new IdResponse(eventId);
    }

    @PatchMapping("/{eventId}/mentee/{menteeId}")
    public void rejectApply(@PathVariable Long eventId, @PathVariable Long menteeId, @RequestBody EventRejectRequest request) {
        eventService.reject(new EventReject(eventId, menteeId, request.rejectMessage()));
    }

    @PatchMapping("/{eventId}/resumes/{resumeId}/complete")
    public void completeReview(
            @PathVariable Long eventId, @PathVariable Long resumeId, @RequestBody CompleteEventRequest request, @AuthenticationPrincipal JwtUser user
    ) {
        eventService.completeReview(eventId, request.comment(), resumeId);
    }

    @GetMapping("/{eventId}")
    public EventInfoResponse getOne(@PathVariable Long eventId) {
        Event event = eventService.getOne(eventId);
        List<EventPosition> positions = eventPositionService.getAll(eventId);

        return new EventInfoResponse(event, positions);
    }

    @GetMapping
    public EventPageResponse getAll(Pageable pageable) {
        Page<Event> pageAbleEvent = eventService.getAllWithPage(new AllEventFilter(null, null), pageable);

        List<Event> events = getEvents(pageAbleEvent);
        Map<Long, List<EventPosition>> positions = getPositions(events);

        List<EventResponse> responses = events.stream()
                .map(event -> new EventResponse(event, positions.get(event.getId())))
                .toList();

        return new EventPageResponse(responses, pageAbleEvent);
    }

    private static List<Event> getEvents(Page<Event> events) {
        List<Event> content = new ArrayList<>(events.getContent());
        Collections.sort(content);

        return content;
    }

    private Map<Long, List<EventPosition>> getPositions(List<Event> content) {
        List<Long> eventIds = content.stream()
                .map(Event::getId)
                .toList();

        return eventPositionService.getAll(eventIds);
    }

}
