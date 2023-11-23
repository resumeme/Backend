package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDateTime;
import java.util.List;

import static org.devcourse.resumeme.common.util.Validator.notNull;

public record EventUpdateRequest(EventInfoRequest info, EventTimeRequest time, List<String> positions) {

    public EventUpdateVo toVo(Long eventId) {
        notNull(positions);
        List<Position> positionEntities = positions.stream()
                .map(position -> Position.valueOf(position.toUpperCase()))
                .toList();

        return new EventUpdateVo(eventId, info.title, info.content, info().maximumAttendee, positionEntities,
                time.openDateTime, time.closeDateTime, time.endDate);
    }

    public record EventInfoRequest(String title, String content, int maximumAttendee) {

    }

    public record EventTimeRequest(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {

    }

}
