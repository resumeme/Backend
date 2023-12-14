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
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.business.event.domain.Progress.APPLY;
import static org.devcourse.resumeme.business.event.domain.Progress.COMPLETE;
import static org.devcourse.resumeme.business.event.domain.Progress.FEEDBACK_COMPLETE;
import static org.devcourse.resumeme.business.event.domain.Progress.REJECT;
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
    @Enumerated(STRING)
    private Progress progress;

    @Getter
    private String rejectMessage;

    @Getter
    private String overallReview;

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

    public boolean isSameResume(Long resumeId) {
        return this.resumeId.equals(resumeId);
    }

    public void reject(String rejectMessage) {
        notNull(rejectMessage);

        this.rejectMessage = rejectMessage;
        this.progress = REJECT;
    }

    public boolean isRejected() {
        return this.progress.equals(REJECT);
    }

    public boolean isAttending() {
        return this.progress.attending();
    }

    public void completeEvent(String overallReview) {
        notNull(overallReview);

        if (isRejected()) {
            throw new EventException(ExceptionCode.EVENT_REJECTED);
        }

        if (progress != COMPLETE) {
            this.overallReview = overallReview;
            this.progress = FEEDBACK_COMPLETE;
        }
    }

    public void editComplete() {
        this.progress = COMPLETE;
    }

    public boolean isReject() {
        return progress.isReject();
    }

}
