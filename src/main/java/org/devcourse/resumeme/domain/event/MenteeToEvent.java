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
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = PROTECTED)
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
        validate(event == null, "NOT_EMPTY_VALUE", "이벤트는 빈 값일 수 없습니다");
        validate(menteeId == null, "NOT_EMPTY_VALUE", "멘티는 빈 값일 수 없습니다");

        this.event = event;
        this.menteeId = menteeId;
    }

    public boolean isSameMentee(Long menteeId) {
        return this.menteeId.equals(menteeId);
    }

}
