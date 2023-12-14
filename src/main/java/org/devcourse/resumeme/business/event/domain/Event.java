package org.devcourse.resumeme.business.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.exception.CustomException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.business.event.domain.EventStatus.FINISH;
import static org.devcourse.resumeme.common.util.Validator.notNull;
import static org.devcourse.resumeme.global.exception.ExceptionCode.DUPLICATED_EVENT_OPEN;
import static org.devcourse.resumeme.global.exception.ExceptionCode.DUPLICATE_APPLICATION_EVENT;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_OPEN_TIME;
import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Event extends BaseEntity implements Comparable<Event> {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Getter
    @Embedded
    private EventInfo eventInfo;

    @Getter
    @Embedded
    private EventTimeInfo eventTimeInfo;

    @Getter
    private Long mentorId;

    @OneToMany(mappedBy = "event", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<EventPosition> positions = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "event", cascade = {PERSIST})
    private List<MenteeToEvent> applicants = new ArrayList<>();

    public Event(EventInfo eventInfo, EventTimeInfo eventTimeInfo, Long mentorId, List<Position> positions) {
        validateInput(eventInfo, eventTimeInfo, mentorId, positions);

        this.eventInfo = eventInfo;
        this.eventTimeInfo = eventTimeInfo;
        this.mentorId = mentorId;
        this.positions = getPositions(positions);
    }

    private List<EventPosition> getPositions(List<Position> positions) {
        return IntStream.range(0, positions.size())
                .mapToObj(i -> new EventPosition(positions.get(i), this, i))
                .toList();
    }

    private void validateInput(EventInfo eventInfo, EventTimeInfo eventTimeInfo, Long mentorId, List<Position> positions) {
        notNull(eventInfo);
        notNull(eventTimeInfo);
        notNull(mentorId);
        notNull(positions);
    }

    public int acceptMentee(Long menteeId, Long resumeId) {
        checkDuplicateApplicationEvent(menteeId);
        eventInfo.checkAvailableApplication();
        applicants.add(new MenteeToEvent(this, menteeId, resumeId));

        int currentApplicantSize = getApplicantsCount();

        return eventInfo.close(currentApplicantSize);
    }

    public int getApplicantsCount() {
        return (int) applicants.stream()
                .filter(applicant -> !applicant.isReject())
                .count();
    }

    private void checkDuplicateApplicationEvent(Long menteeId) {
        applicants.stream()
                .filter(applicant -> applicant.isSameMentee(menteeId) && !applicant.isReject())
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

    public void complete(Long resumeId, String message) {
        MenteeToEvent menteeToEvent = applicants.stream()
                .filter(m -> m.isSameResume(resumeId))
                .findFirst()
                .orElseThrow(() -> new CustomException(RESUME_NOT_FOUND));

        menteeToEvent.completeEvent(message);
    }

    public int reOpenEvent() {
        eventInfo.reOpen(applicants.size());

        return eventInfo.remainSeats(applicants.size());
    }

    public void checkOpen() {
        if (eventInfo.isOpen()) {
            throw new EventException(DUPLICATED_EVENT_OPEN);
        }
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

    @Override
    public int compareTo(Event other) {
        if (other.eventInfo.getStatus().equals(FINISH)) {
            return -1;
        }

        return other.eventTimeInfo.getOpenDateTime().compareTo(eventTimeInfo.getOpenDateTime());
    }

    public void updateInfo(String title, String content, int maximumAttendee) {
        this.eventInfo.update(title, content, maximumAttendee);
    }

    public void updatePosition(List<Position> positions) {
        for (int i = 0; i < this.positions.size();) {
            this.positions.remove(i);
        }

        List<EventPosition> newPositions = getNewPosition(positions);
        this.positions.addAll(newPositions);
    }

    private List<EventPosition> getNewPosition(List<Position> positions) {
        List<EventPosition> newPositions = getPositions(positions);

        return new ArrayList<>(newPositions);
    }

    public void updateTimeInfo(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDateTime) {
        this.eventTimeInfo.update(openDateTime, closeDateTime, endDateTime);
    }

    public void checkDate() {
        LocalDateTime now = LocalDateTime.now();
        eventTimeInfo.checkDate(now);
    }

    public boolean canUpdate() {
        return this.eventInfo.getStatus().isReady() || this.applicants.isEmpty();
    }

}
