package org.devcourse.resumeme.business.event.repository;

import jakarta.persistence.LockModeType;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"applicants"})
    Optional<Event> findWithLockById(Long id);

    List<Event> findAllByMentorId(Long mentorId);

    @EntityGraph(attributePaths = {"applicants"})
    Optional<Event> findWithApplicantsById(Long id);

    @EntityGraph(attributePaths = {"applicants"})
    Page<Event> findAllByMentorIdOrderByCreatedDateDesc(Long mentorId, Pageable pageable);

    @EntityGraph(attributePaths = {"applicants"})
    Page<Event> findAllByOrderByCreatedDateDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"applicants"})
    Page<Event> findAllByApplicantsMenteeIdOrderByCreatedDateDesc(Long menteeId, Pageable pageable);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.openDateTime <= :now and e.eventInfo.status = 'READY'")
    void openBookedEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.closeDateTime <= :now and e.eventInfo.status = 'OPEN'")
    void closeApplyToEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.endDate <= :now and e.eventInfo.status = 'CLOSE'")
    void finishEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

}
