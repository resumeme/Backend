package org.devcourse.resumeme.domain.event;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private long maximumAttendee;

    private String title;

    private String content;

    private LocalDate finishedDate;

    private Position position;

    private Long mentorId;

    @OneToMany(mappedBy = "event", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<MenteeToEvent> attendedMentees = new ArrayList<>();

    public Event(long maximumAttendee, String title, String content, LocalDate finishedDate, Position position, Long mentorId) {
        this.maximumAttendee = maximumAttendee;
        this.title = title;
        this.content = content;
        this.finishedDate = finishedDate;
        this.position = position;
        this.mentorId = mentorId;
    }

}
