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

    public void checkAvailableApplication() {
        if (status.isClosed()) {
            throw new EventException("NO_REMAIN_SEATS", "이미 모든 신청이 마감되었습니다");
        }
    }

    public void close(int attendeeCount) {
        if (attendeeCount == maximumAttendee) {
            status = EventStatus.CLOSE;
        }
    }

    public void reOpen(int attendeeCount) {
        if (attendeeCount >= maximumAttendee) {
            throw new EventException("NO_AVAILABLE_SEATS", "잔여 자리가 없어서 재 오픈이 불가능합니다");
        }

        status = EventStatus.REOPEN;
    }

    public void open() {
        this.status = EventStatus.OPEN;
    }

}