package org.devcourse.resumeme.business.event.repository;

import jakarta.persistence.LockModeType;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
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

    List<Event> findAllByMentor(Mentor mentor);

    @EntityGraph(attributePaths = {"applicants"})
    Optional<Event> findWithApplicantsById(Long id);

    @EntityGraph(attributePaths = {"applicants", "mentor"})
    List<Event> findAllByMentorId(Long mentorId);

    @EntityGraph(attributePaths = {"positions", "mentor"})
    List<Event> findAll();

    @EntityGraph(attributePaths = {"applicants", "mentor"})
    List<Event> findAllByApplicantsMenteeId(Long menteeId);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.openDateTime <= :now")
    void openBookedEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.closeDateTime <= :now")
    void closeApplyToEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

    @Modifying
    @Query("update Event e set e.eventInfo.status = :status where e.eventTimeInfo.endDate <= :now")
    void finishEvent(@Param("status") EventStatus status, @Param("now")LocalDateTime now);

}
