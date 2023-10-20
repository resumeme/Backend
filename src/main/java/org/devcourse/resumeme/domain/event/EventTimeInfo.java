package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.event.exception.EventException;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class EventTimeInfo {

    private LocalDateTime openDateTime;

    private LocalDateTime endDate;

    private EventTimeInfo(LocalDateTime openDateTime, LocalDateTime endDate) {
        this.openDateTime = openDateTime;
        this.endDate = endDate;
    }

    public static EventTimeInfo onStart(LocalDateTime endDate) {
        return new EventTimeInfo(LocalDateTime.now(), endDate);
    }

    public static EventTimeInfo book(LocalDateTime openDateTime, LocalDateTime endDate) {
        if (openDateTime.isBefore(LocalDateTime.now())) {
            throw new EventException("CAN_NOT_RESERVATION", "현재 시간보다 이전 시간으로는 예약할 수 없습니다");
        }

        return new EventTimeInfo(openDateTime, endDate);
    }

    public boolean isAfterOpenTime(LocalDateTime nowDateTime) {
        return nowDateTime.isAfter(this.openDateTime);
    }

}