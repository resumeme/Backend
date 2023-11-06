package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public record EventsResponse(
        Long eventId,
        String nickname,
        String title,
        LocalDateTime endDate,
        String status,
        List<String> positions
) {

    public EventsResponse(Event event) {
        this(event.getId(), event.getMentor().getRequiredInfo().getNickname(), event.title(),
                event.endDate(), event.status(), event.positions());
    }

}
