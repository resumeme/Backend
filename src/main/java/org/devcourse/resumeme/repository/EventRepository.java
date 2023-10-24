package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(PESSIMISTIC_WRITE)
    Optional<Event> findWithLockById(Long eventId);

}
