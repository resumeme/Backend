package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class EventPosition {

    @Id
    @GeneratedValue
    @Column(name = "event_position_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public EventPosition(Position position, Event event) {
        validate(position == null, "NO_EMPTY_VALUE", "포지션은 필수 값입니다");
        validate(event == null, "NO_EMPTY_VALUE", "이벤트는 필수 값입니다");

        this.position = position;
        this.event = event;
    }

}
