package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class MenteeToEvent extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "mentee_to_event_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private Long menteeId;

    public MenteeToEvent(Event event, Long menteeId) {
        this.event = event;
        this.menteeId = menteeId;
    }

}
