package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDateTime;
import java.util.List;

import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;

public record EventCreateRequest(EventInfoRequest info, EventTimeRequest time, List<String> positions) {

    public Event toEntity(Long mentorId) {
        check(positions == null || positions.isEmpty(), NO_EMPTY_VALUE);

        List<Position> positionEntities = positions.stream()
                .map(position -> Position.valueOf(position.toUpperCase()))
                .toList();

        return new Event(info.toEntity(time.openDateTime), time.toEntity(), mentorId, positionEntities);
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
