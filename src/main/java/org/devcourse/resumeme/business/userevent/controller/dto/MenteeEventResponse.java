package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;

import java.time.LocalDateTime;

public record MenteeEventResponse(Long resumeId, String status, String title,
                                  String mentorName, LocalDateTime startDate, LocalDateTime endDate) {

    public MenteeEventResponse(MenteeToEvent applicant, Event event) {
        this(applicant.getResumeId(), applicant.getProgress().name(), event.getEventInfo().getTitle(),
                event.getMentor().getRequiredInfo().getRealName(), event.getEventTimeInfo().getOpenDateTime(), event.getEventTimeInfo().getCloseDateTime());
    }

}
