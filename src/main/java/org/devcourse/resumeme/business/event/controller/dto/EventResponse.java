package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;

import java.util.List;

public record EventResponse(EventInfoResponse info, MentorInfo mentorInfo) {

    public EventResponse(Event event, List<EventPosition> positions, UserResponse mentor) {
        this(new EventInfoResponse(event, positions), new MentorInfo(mentor));
    }

    record MentorInfo(Long mentorId, String nickname, String imageUrl) {

        MentorInfo(UserResponse mentor) {
            this(mentor.userId(), mentor.nickname(), mentor.imageUrl());
        }
    }

}
