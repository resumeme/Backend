package org.devcourse.resumeme.domain.event;

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
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.domain.mentor.Mentor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

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

    @Embedded
    private EventTimeInfo eventTimeInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "event", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<EventPosition> positions = new ArrayList<>();

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
        validate(eventInfo == null, "NO_EMPTY_VALUE", "빈 값일 수 없습니다");
        validate(eventTimeInfo == null, "NO_EMPTY_VALUE", "빈 값일 수 없습니다");
        validate(mentor == null, "NO_EMPTY_VALUE", "빈 값일 수 없습니다");
        validate(positions == null, "NO_EMPTY_VALUE", "빈 값일 수 없습니다");
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
                    throw new EventException("DUPLICATE_APPLICATION_EVENT", "이미 신청한 이력이 있습니다");
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
                    throw new EventException("NOT_FOUND_MENTEE", "신청한 멘티가 없습니다");
                });

        return eventInfo.remainSeats(applicants.size());
    }

    public int reOpenEvent() {
        eventInfo.reOpen(applicants.size());

        return eventInfo.remainSeats(applicants.size());
    }

    public void openReservationEvent(LocalDateTime nowDateTime) {
        if (!eventTimeInfo.isAfterOpenTime(nowDateTime)) {
            throw new EventException("NOT_OPEN_TIME", "예약한 오픈 시간이 아닙니다");
        }

        eventInfo.open();
    }

    public Long getApplicantId(Long menteeId) {
        for (MenteeToEvent applicant : applicants) {
            if (applicant.isSameMentee(menteeId)) {
                return applicant.getId();
            }
        }

        throw new EventException("NOT_FOUND", "이력을 찾을 수 없습니다");
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
                        () -> {throw new EventException("MENTEE_NOT_FOUND", "참여중인 멘티가 없습니다");}
                );
    }

}
