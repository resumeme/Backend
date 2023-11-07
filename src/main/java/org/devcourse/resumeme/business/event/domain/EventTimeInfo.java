package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.exception.EventException;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.common.util.Validator.notNull;
import static org.devcourse.resumeme.global.exception.ExceptionCode.CAN_NOT_RESERVATION;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TIME_ERROR;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class EventTimeInfo {

    @Getter
    private LocalDateTime openDateTime;

    @Getter
    private LocalDateTime closeDateTime;

    @Getter
    private LocalDateTime endDate;

    private EventTimeInfo(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        this.closeDateTime = closeDateTime;
        validateInput(openDateTime, closeDateTime, endDate);

        this.openDateTime = openDateTime;
        this.endDate = endDate;
    }

    private void validateInput(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        notNull(openDateTime);
        notNull(closeDateTime);
        notNull(endDate);
        check(openDateTime.isAfter(closeDateTime), TIME_ERROR);
        check(closeDateTime.isAfter(endDate), TIME_ERROR);
    }

    public static EventTimeInfo onStart(LocalDateTime nowDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        return new EventTimeInfo(nowDateTime, closeDateTime, endDate);
    }

    public static EventTimeInfo book(LocalDateTime nowDateTime, LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        notNull(openDateTime);
        if (openDateTime.isBefore(nowDateTime)) {
            throw new EventException(CAN_NOT_RESERVATION);
        }

        return new EventTimeInfo(openDateTime, closeDateTime, endDate);
    }

    public boolean isAfterOpenTime(LocalDateTime nowDateTime) {
        return nowDateTime.isAfter(this.openDateTime);
    }

}
