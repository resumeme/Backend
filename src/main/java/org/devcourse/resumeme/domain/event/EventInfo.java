package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.event.exception.EventException;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class EventInfo {

    private int maximumAttendee;

    private String title;

    private String content;

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
        validate(maximumAttendee < 2 || maximumAttendee > 10, "RANGE_MAXIMUM_ATTENDEE", "참여 인원 수를 2~10명 사이에서 정해주세요");
        validate(isBlank(title), "NO_EMPTY_STRING", "제목은 필수 값입니다");
        validate(isBlank(content), "NO_EMPTY_STRING", "내용은 필수 값입니다");
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

    public int close(int attendeeCount) {
        if (attendeeCount == maximumAttendee) {
            status = EventStatus.CLOSE;
        }

        return maximumAttendee - attendeeCount;
    }

    public void reOpen(int attendeeCount) {
        if (attendeeCount >= maximumAttendee) {
            throw new EventException("NO_AVAILABLE_SEATS", "잔여 자리가 없어서 재 오픈이 불가능합니다");
        }

        status = EventStatus.REOPEN;
    }

    public void open() {
        if (!status.isBook()) {
            throw new EventException("CANNOT_OPEN", "예약한 이벤트에 한에서만 오픈 신청읋 할 수 있습니다");
        }

        status = EventStatus.OPEN;
    }

    public int remainSeats(int attendedMenteeCount) {
        return maximumAttendee - attendedMenteeCount;
    }

}
