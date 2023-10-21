package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.event.exception.EventException;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class EventTimeInfo {

    private LocalDateTime openDateTime;

    private LocalDateTime endDate;

    private EventTimeInfo(LocalDateTime openDateTime, LocalDateTime endDate) {
        validateInput(openDateTime, endDate);

        this.openDateTime = openDateTime;
        this.endDate = endDate;
    }

    private void validateInput(LocalDateTime openDateTime, LocalDateTime endDate) {
        validate(openDateTime == null, "NO_EMPTY_VALUE", "시작 시간은 빈 값일 수 없습니다");
        validate(endDate == null, "NO_EMPTY_VALUE", "종료 시간은 빈 값일 수 없습니다");
        validate(openDateTime.isAfter(endDate), "TIME_ERROR", "시작 시간이 종료 시간보다 빨라야 합니다");
    }

    public static EventTimeInfo onStart(LocalDateTime nowDateTime, LocalDateTime endDate) {
        return new EventTimeInfo(nowDateTime, endDate);
    }

    public static EventTimeInfo book(LocalDateTime nowDateTime, LocalDateTime openDateTime, LocalDateTime endDate) {
        validate(openDateTime == null, "NO_EMPTY_VALUE", "시작 시간은 빈 값일 수 없습니다");
        if (openDateTime.isBefore(nowDateTime)) {
            throw new EventException("CAN_NOT_RESERVATION", "현재 시간보다 이전 시간으로는 예약할 수 없습니다");
        }

        return new EventTimeInfo(openDateTime, endDate);
    }

    public boolean isAfterOpenTime(LocalDateTime nowDateTime) {
        return nowDateTime.isAfter(this.openDateTime);
    }

}
