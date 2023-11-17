package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public record MenteeResumeResponse(Long resumeId, String title, LocalDateTime lastModifiedDate, List<EventResponse> events) {

    public void add(Event event) {
        this.events.add(new EventResponse(event.getMentor().getRequiredInfo().getRealName(), event.getEventInfo().getTitle(), event.getCreatedDate(), event.status()));
    }

    record EventResponse(String mentorName, String eventTitle, LocalDateTime createdDate, String status) {

    }

}
