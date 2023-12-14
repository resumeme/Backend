package org.devcourse.resumeme.business.event.repository;

import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenteeToEventRepository extends JpaRepository<MenteeToEvent, Long> {

    @EntityGraph(attributePaths = {"event", "event.applicants"})
    List<MenteeToEvent> findByMenteeId(Long menteeId);

}
