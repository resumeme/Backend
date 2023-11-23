package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.exception.EventException;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.CANNOT_OPEN_EVENT;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_AVAILABLE_SEATS;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_REMAIN_SEATS;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RANGE_MAXIMUM_ATTENDEE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TEXT_LENGTH_ERROR;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class EventInfo {

    @Getter
    private int maximumAttendee;

    @Getter
    private String title;

    @Getter
    private String content;

    @Getter
    @Enumerated(STRING)
    private EventStatus status;

    private EventInfo(int maximumAttendee, String title, String content, EventStatus status) {
        validateInput(maximumAttendee, title, content);

        this.maximumAttendee = maximumAttendee;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    private void validateInput(int maximumAttendee, String title, String content) {
        check(maximumAttendee < 2 || maximumAttendee > 10, RANGE_MAXIMUM_ATTENDEE);
        check(isBlank(title) || title.length() > 30, TEXT_LENGTH_ERROR);
        check(isBlank(content), NO_EMPTY_VALUE);
    }

    public static EventInfo open(int maximumAttendee, String title, String content) {
        return new EventInfo(maximumAttendee, title, content, EventStatus.OPEN);
    }

    public static EventInfo book(int maximumAttendee, String title, String content) {
        return new EventInfo(maximumAttendee, title, content, EventStatus.READY);
    }

    public void checkAvailableApplication() {
        if (!status.canApply()) {
            throw new EventException(NO_REMAIN_SEATS);
        }
    }

    public int close(int attendeeCount) {
        if (attendeeCount == maximumAttendee) {
            status = EventStatus.CLOSE;
        }

        return maximumAttendee - attendeeCount;
    }

    public void reOpen(int attendeeCount) {
        if (attendeeCount >= maximumAttendee) {
            throw new EventException(NO_AVAILABLE_SEATS);
        }

        status = EventStatus.REOPEN;
    }

    public void open() {
        if (!status.isReady()) {
            throw new EventException(CANNOT_OPEN_EVENT);
        }

        status = EventStatus.OPEN;
    }

    public int remainSeats(int attendedMenteeCount) {
        return maximumAttendee - attendedMenteeCount;
    }

    public boolean isOpen() {
        return this.status.isOpen();
    }

    public void update(String title, String content, int maximumAttendee) {
        validateInput(maximumAttendee, title, content);

        this.title = title;
        this.content = content;
        this.maximumAttendee = maximumAttendee;
    }

}
