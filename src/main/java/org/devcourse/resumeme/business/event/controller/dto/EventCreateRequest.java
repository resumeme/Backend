package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDateTime;
import java.util.List;

public record EventCreateRequest(EventInfoRequest info, EventTimeRequest time, List<String> positions) {

    public Event toEntity(Mentor mentor) {
        List<Position> positionEntities = positions.stream()
                .map(position -> Position.valueOf(position.toUpperCase()))
                .toList();

        return new Event(info.toEntity(time.openDateTime), time.toEntity(), mentor, positionEntities);
    }

    public record EventInfoRequest(String title, String content, int maximumAttendee) {

        public EventInfo toEntity(LocalDateTime openDateTime) {
            if (openDateTime == null) {
                return EventInfo.open(maximumAttendee, title, content);
            }

            return EventInfo.book(maximumAttendee, title, content);
        }

    }

    public record EventTimeRequest(LocalDateTime now, LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {

        public EventTimeInfo toEntity() {
            if (openDateTime == null) {
                return EventTimeInfo.onStart(now, closeDateTime, endDate);
            }

            return EventTimeInfo.book(now, openDateTime, closeDateTime, endDate);
        }

    }

}
