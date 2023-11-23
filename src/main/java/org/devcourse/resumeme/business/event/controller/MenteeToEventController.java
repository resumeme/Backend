package org.devcourse.resumeme.business.event.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.controller.dto.v2.ApplyUpdateRequest;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.event.service.vo.ApplyUpdateVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appliments/events")
public class MenteeToEventController {

    private final MenteeToEventService applyService;

    @PatchMapping("/{eventId}")
    public void updateEvent(@PathVariable Long eventId, @RequestBody ApplyUpdateRequest request) {
        ApplyUpdateVo applyUpdateVo = request.toVo();
        applyService.update(eventId, applyUpdateVo);
    }

}
