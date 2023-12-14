package org.devcourse.resumeme.business.event.repository;

import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventPositionRepository extends JpaRepository<EventPosition, Long> {

    @Query("select ep from EventPosition ep where ep.event.id = :eventId")
    List<EventPosition> findAllByEventId(@Param("eventId") Long eventId);

    @Query("select ep from EventPosition ep where ep.event.id in :eventIds")
    List<EventPosition> findAllByEventIds(@Param("eventIds") List<Long> eventIds);

}
