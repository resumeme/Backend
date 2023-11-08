package org.devcourse.resumeme.business.event.domain;

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
import static org.devcourse.resumeme.business.event.domain.Progress.APPLY;
import static org.devcourse.resumeme.business.event.domain.Progress.REJECT;
import static org.devcourse.resumeme.business.event.domain.Progress.REQUEST;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MenteeToEvent extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentee_to_event_id")
    private Long id;

    @Getter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Getter
    private Long menteeId;

    @Getter
    private Long resumeId;

    @Getter
    private Progress progress;

    private String rejectMessage;

    public MenteeToEvent(Event event, Long menteeId, Long resumeId) {
        notNull(event);
        notNull(menteeId);
        notNull(resumeId);

        this.event = event;
        this.menteeId = menteeId;
        this.resumeId = resumeId;
        this.progress = APPLY;
        this.rejectMessage = "";
    }

    public boolean isSameMentee(Long menteeId) {
        return this.menteeId.equals(menteeId);
    }

    public void reject(String rejectMessage) {
        notNull(rejectMessage);

        this.rejectMessage = rejectMessage;
        this.progress = REJECT;
        this.event = null;
    }

    public boolean isRejected() {
        return this.progress.equals(REJECT);
    }

    public void requestReview() {
        this.progress = REQUEST;
    }

}
