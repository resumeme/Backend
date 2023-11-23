package org.devcourse.resumeme.business.event.service.vo;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EventUpdateVo {

    private final Long eventId;

    private String title;

    private String content;

    private int maximumAttendee;

    private final List<Position> positions;

    private final LocalDateTime openDateTime;

    private final LocalDateTime closeDateTime;

    private final LocalDateTime endDateTime;

    public EventUpdateVo(Long eventId, String title, String content, int maximumAttendee, List<Position> positions,
            LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.content = content;
        this.maximumAttendee = maximumAttendee;
        this.positions = positions;
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.endDateTime = endDateTime;
    }

    public void update(Event event) {
        event.updateInfo(title, content, maximumAttendee);
        event.updatePosition(positions);
        event.updateTimeInfo(openDateTime, closeDateTime, endDateTime);
    }

}
