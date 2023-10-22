package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

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

    public Event(int maximumAttendee, String title, String content, LocalDateTime openDateTime, LocalDateTime endDate, Position position, Long mentorId) {
        this.maximumAttendee = maximumAttendee;
        this.title = title;
        this.content = content;
        this.openDateTime = openDateTime;
        this.endDate = endDate;
        this.position = position;
        this.mentorId = mentorId;
    }

}
