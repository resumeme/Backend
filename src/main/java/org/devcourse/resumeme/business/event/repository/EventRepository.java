package org.devcourse.resumeme.business.event.repository;

import jakarta.persistence.LockModeType;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findWithLockById(Long id);

    List<Event> findAllByMentor(Mentor mentor);

    @EntityGraph(attributePaths = {"applicants"})
    Optional<Event> findWithApplicantsById(Long id);

    @EntityGraph(attributePaths = {"applicants", "mentor"})
    List<Event> findAllByMentorId(Long mentorId);

    @EntityGraph(attributePaths = {"positions", "mentor"})
    List<Event> findAll();

    @Query("select e from Event e join fetch e.applicants ea join fetch e.mentor m where ea.resumeId in :resumeIds")
    List<Event> findAllByApplicantsResumeIds(@Param("resumeIds") List<Long> resumeIds);

}
