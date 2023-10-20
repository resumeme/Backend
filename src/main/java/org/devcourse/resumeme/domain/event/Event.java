package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.domain.metor.Mentor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class Event extends BaseEntity {

    @Id
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
    private List<MenteeToEvent> attendedMentees = new ArrayList<>();

    public Event(EventInfo eventInfo, EventTimeInfo eventTimeInfo, Mentor mentor, List<EventPosition> positions) {
        this.eventInfo = eventInfo;
        this.eventTimeInfo = eventTimeInfo;
        this.mentor = mentor;
        this.positions = positions;
    }

    public void applicationToEvent(Long menteeId) {
        checkDuplicateApplicationEvent(menteeId);
        eventInfo.checkAvailableApplication();
        attendedMentees.add(new MenteeToEvent(this, menteeId));
        eventInfo.close(attendedMentees.size());
    }

    private void checkDuplicateApplicationEvent(Long menteeId) {
        for (MenteeToEvent attendedMentee : attendedMentees) {
            if (attendedMentee.isSameMentee(menteeId)) {
                throw new EventException("DUPLICATE_APPLICATION_EVENT", "이미 신청한 이력이 있습니다");
            }
        }
    }

    public void reOpenEvent() {
        eventInfo.reOpen(attendedMentees.size());
    }

    public void openReservationEvent() {
        if (!eventTimeInfo.isAfterOpenTime(LocalDateTime.now())) {
            throw new EventException("NOT_OPEN_TIME", "예약한 오픈 시간이 아닙니다");
        }

        eventInfo.open();
    }

}
