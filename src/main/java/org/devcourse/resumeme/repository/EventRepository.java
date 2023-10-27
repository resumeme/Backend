package org.devcourse.resumeme.repository;

import jakarta.persistence.LockModeType;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findWithLockById(Long id);

    List<Event> findAllByMentor(Mentor mentor);

}
