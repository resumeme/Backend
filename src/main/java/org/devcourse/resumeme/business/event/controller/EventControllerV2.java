package org.devcourse.resumeme.business.event.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.v2.EventUpdateRequest;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/events")
public class EventControllerV2 {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public void updateEvent(@PathVariable Long eventId, @RequestBody EventUpdateRequest request) {
        EventUpdateVo eventUpdateVo = request.toVo();
        eventService.update(eventId, eventUpdateVo);
    }

}

