package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.event.exception.EventException;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@NoArgsConstructor
public class EventInfo {

    private int maximumAttendee;

    private String title;

    private String content;

    @Enumerated(STRING)
    private EventStatus status;

    private EventInfo(int maximumAttendee, String title, String content, EventStatus status) {
        this.maximumAttendee = maximumAttendee;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public static EventInfo open(int maximumAttendee, String title, String content) {
        return new EventInfo(maximumAttendee, title, content, EventStatus.OPEN);
    }

    public static EventInfo book(int maximumAttendee, String title, String content) {
        return new EventInfo(maximumAttendee, title, content, EventStatus.BOOK);
    }

}