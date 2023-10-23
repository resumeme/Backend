package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
