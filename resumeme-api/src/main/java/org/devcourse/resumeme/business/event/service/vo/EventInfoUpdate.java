package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import java.time.LocalDateTime;
import java.util.List;

public class EventInfoUpdate extends EventUpdateVo {

    private String title;

    private String content;

    private int maximumAttendee;

    private List<Position> positions;

    private LocalDateTime openDateTime;

    private LocalDateTime closeDateTime;

    private LocalDateTime endDateTime;

    public EventInfoUpdate(Long eventId, String title, String content, int maximumAttendee, List<Position> positions,
            LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDateTime) {
        super(eventId);
        this.title = title;
        this.content = content;
        this.maximumAttendee = maximumAttendee;
        this.positions = positions;
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public void update(Event event) {
        if (!event.canUpdate()) {
            throw new EventException(ExceptionCode.NOT_UPDATE_EVENT);
        }

        event.updateInfo(title, content, maximumAttendee);
        event.updatePosition(positions);
        event.updateTimeInfo(openDateTime, closeDateTime, endDateTime);
    }

}
