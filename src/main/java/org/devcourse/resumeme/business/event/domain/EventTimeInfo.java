package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.exception.EventException;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.common.util.Validator.notNull;
import static org.devcourse.resumeme.global.exception.ExceptionCode.*;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class EventTimeInfo {

    @Getter
    private LocalDateTime openDateTime;

    @Getter
    private LocalDateTime closeDateTime;

    @Getter
    private LocalDateTime endDate;

    private boolean isBook;

    private EventTimeInfo(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate, boolean isBook) {
        this.closeDateTime = closeDateTime;
        validateInput(openDateTime, closeDateTime, endDate);

        this.openDateTime = openDateTime;
        this.endDate = endDate;
        this.isBook = isBook;
    }

    private void validateInput(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        notNull(openDateTime);
        notNull(closeDateTime);
        notNull(endDate);
        check(openDateTime.isAfter(closeDateTime), TIME_ERROR);
        check(closeDateTime.isAfter(endDate), TIME_ERROR);
    }

    public static EventTimeInfo onStart(LocalDateTime nowDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        return new EventTimeInfo(nowDateTime, closeDateTime, endDate, true);
    }

    public static EventTimeInfo book(LocalDateTime nowDateTime, LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        notNull(openDateTime);
        if (openDateTime.isBefore(nowDateTime)) {
            throw new EventException(CAN_NOT_RESERVATION);
        }

        return new EventTimeInfo(openDateTime, closeDateTime, endDate, false);
    }

    public void update(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDateTime) {
        if (isBook) {
            notNull(openDateTime);
        }

        notNull(closeDateTime);
        notNull(endDateTime);
        check(closeDateTime.isAfter(endDateTime), TIME_ERROR);

        this.openDateTime = openDateTime == null ? this.openDateTime : openDateTime;
        this.closeDateTime = closeDateTime;
        this.endDate = endDateTime;
    }

    public void checkDate(LocalDateTime now) {
        check(now.isBefore(openDateTime) || now.isAfter(endDate), NOT_AVAILABLE_COMMENT_TIME);
    }

}
