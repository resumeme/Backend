package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MenteeToEvent extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentee_to_event_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private Long menteeId;

    private Long resumeId;

    public MenteeToEvent(Event event, Long menteeId, Long resumeId) {
        validate(event == null, "NOT_EMPTY_VALUE", "이벤트는 빈 값일 수 없습니다");
        validate(menteeId == null, "NOT_EMPTY_VALUE", "멘티는 빈 값일 수 없습니다");
        validate(resumeId == null, "NOT_EMPTY_VALUE", "이력서는 빈 값일 수 없습니다");

        this.event = event;
        this.menteeId = menteeId;
        this.resumeId = resumeId;
    }

    public boolean isSameMentee(Long menteeId) {
        return this.menteeId.equals(menteeId);
    }

}
