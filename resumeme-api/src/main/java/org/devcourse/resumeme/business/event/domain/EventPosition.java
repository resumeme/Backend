package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class EventPosition implements Comparable<EventPosition> {

    @Id
    @GeneratedValue
    @Column(name = "event_position_id")
    private Long id;

    @Getter
    @Enumerated(STRING)
    private Position position;

    @Getter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "position_order")
    private int order;

    public EventPosition(Position position, Event event, int order) {
        notNull(position);
        notNull(event);

        this.position = position;
        this.event = event;
        this.order = order;
    }

    @Override
    public int compareTo(EventPosition other) {
        return Integer.compare(this.order, other.order);
    }

}
