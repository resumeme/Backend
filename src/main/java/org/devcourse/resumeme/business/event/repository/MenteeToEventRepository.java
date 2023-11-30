package org.devcourse.resumeme.business.event.repository;

import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenteeToEventRepository extends JpaRepository<MenteeToEvent, Long> {

    @Query("select me from MenteeToEvent me join fetch me.event where me.resumeId = :resumeId")
    List<MenteeToEvent> findAllByResumeId(@Param(value = "resumeId") Long resumeId);

    @EntityGraph(attributePaths = "event")
    List<MenteeToEvent> findByMenteeId(Long menteeId);

}
