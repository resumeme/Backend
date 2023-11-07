package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.time.LocalDateTime;
import java.util.List;

public record EventsResponse(
        EventInfo eventInfo,
        MentorInfo mentorInfo
) {

    public EventsResponse(Event event) {
        this(new EventInfo(event), new MentorInfo(event.getMentor()));
    }

    record EventInfo(
            Long eventId,
            String title,
            LocalDateTime endDate,
            String status,
            List<String> positions
    ) {

        EventInfo(Event event) {
            this(event.getId(), event.title(), event.endDate(), event.status(), event.positions());
        }

    }

    record MentorInfo(Long mentorId, String nickname, String imageUrl) {

        MentorInfo(Mentor mentor) {
            this(mentor.getId(), mentor.getRequiredInfo().getNickname(), mentor.getImageUrl());
        }
    }


}
