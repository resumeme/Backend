package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.notNull;
import static org.devcourse.resumeme.global.exception.ExceptionCode.APPLICATION_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.DUPLICATE_APPLICATION_EVENT;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_OPEN_TIME;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Event extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Embedded
    private EventInfo eventInfo;

    @Getter
    @Embedded
    private EventTimeInfo eventTimeInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "event", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<EventPosition> positions = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "event", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<MenteeToEvent> applicants = new ArrayList<>();

    public Event(EventInfo eventInfo, EventTimeInfo eventTimeInfo, Mentor mentor, List<Position> positions) {
        validateInput(eventInfo, eventTimeInfo, mentor, positions);

        this.eventInfo = eventInfo;
        this.eventTimeInfo = eventTimeInfo;
        this.mentor = mentor;
        this.positions = positions.stream()
                .map(position -> new EventPosition(position, this))
                .toList();
    }

    private void validateInput(EventInfo eventInfo, EventTimeInfo eventTimeInfo, Mentor mentor, List<Position> positions) {
        notNull(eventInfo);
        notNull(eventTimeInfo);
        notNull(mentor);
        notNull(positions);
    }

    public int acceptMentee(Long menteeId, Long resumeId) {
        checkDuplicateApplicationEvent(menteeId);
        eventInfo.checkAvailableApplication();
        applicants.add(new MenteeToEvent(this, menteeId, resumeId));

        return eventInfo.close(applicants.size());
    }

    private void checkDuplicateApplicationEvent(Long menteeId) {
        applicants.stream()
                .filter(applicant -> applicant.isSameMentee(menteeId))
                .findFirst()
                .ifPresent(applicant -> {
                    throw new EventException(DUPLICATE_APPLICATION_EVENT);
                });
    }

    public int reject(Long menteeId, String message) {
        applicants.stream()
                .filter(applicant -> applicant.isSameMentee(menteeId))
                .findFirst()
                .ifPresentOrElse(applicant -> {
                    applicant.reject(message);
                    applicants.remove(applicant);
                }, () -> {
                    throw new EventException(MENTEE_NOT_FOUND);
                });

        return eventInfo.remainSeats(applicants.size());
    }

    public int reOpenEvent() {
        eventInfo.reOpen(applicants.size());

        return eventInfo.remainSeats(applicants.size());
    }

    public void openReservationEvent(LocalDateTime nowDateTime) {
        if (!eventTimeInfo.isAfterOpenTime(nowDateTime)) {
            throw new EventException(NOT_OPEN_TIME);
        }

        eventInfo.open();
    }

    public Long getApplicantId(Long menteeId) {
        for (MenteeToEvent applicant : applicants) {
            if (applicant.isSameMentee(menteeId)) {
                return applicant.getId();
            }
        }

        throw new EventException(APPLICATION_NOT_FOUND);
    }

    public Mentor getMentor() {
        return mentor;
    }

    public boolean isOpen() {
        return eventInfo.isOpen();
    }

    public void requestReview(Long menteeId) {
        applicants.stream()
                .filter(applicant -> applicant.isSameMentee(menteeId))
                .findFirst()
                .ifPresentOrElse(
                        MenteeToEvent::requestReview,
                        () -> {
                            throw new EventException(MENTEE_NOT_FOUND);
                        }
                );
    }

    public String title() {
        return eventInfo.getTitle();
    }

    public String content() {
        return eventInfo.getContent();
    }

    public int maximumCount() {
        return eventInfo.getMaximumAttendee();
    }

    public LocalDateTime endDate() {
        return eventTimeInfo.getEndDate();
    }

    public List<String> positions() {
        return this.positions.stream()
                .map(eventPosition -> eventPosition.getPosition().name())
                .toList();
    }

    public String status() {
        return eventInfo.getStatus().name();
    }

    public void checkAppliedResume(Long resumeId) {
        for (MenteeToEvent applicant : applicants) {
            if (applicant.getResumeId().equals(resumeId)) {
                return;
            }
        }

        throw new EventException(RESUME_NOT_FOUND);
    }

}
