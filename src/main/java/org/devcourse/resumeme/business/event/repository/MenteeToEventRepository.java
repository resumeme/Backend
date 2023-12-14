package org.devcourse.resumeme.business.event.repository;

import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenteeToEventRepository extends JpaRepository<MenteeToEvent, Long> {

    @EntityGraph(attributePaths = {"event"})
    List<MenteeToEvent> findByMenteeId(Long menteeId);

    @EntityGraph(attributePaths = {"event"})
    List<MenteeToEvent> findByEventMentorId(Long mentorId);

    @EntityGraph(attributePaths = {"event"})
    Page<MenteeToEvent> findAllByOrderByEventCreatedDateDesc(Pageable pageable);

}
