package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;

import java.time.LocalDateTime;

public record MenteeEventResponse(Long eventId, Long resumeId, String status, String title, String rejectMessage, String resumeTitle,
                                  String mentorName, LocalDateTime startDate, LocalDateTime endDate) {

    public MenteeEventResponse(MenteeToEvent applicant, Event event, String mentorName, String resumeTitle) {
        this(event.getId(), applicant.getResumeId(), applicant.getProgress().name(), event.getEventInfo().getTitle(), applicant.getRejectMessage(), resumeTitle,
                mentorName, event.getEventTimeInfo().getOpenDateTime(), event.getEventTimeInfo().getCloseDateTime());
    }

    public MenteeEventResponse(MenteeToEvent menteeToEvent, String mentorName, String resumeTitle) {
        this(menteeToEvent, menteeToEvent.getEvent(), mentorName, resumeTitle);
    }

}
