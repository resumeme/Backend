package org.devcourse.resumeme.business.user.repository.mentor;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findByEmail(String email);

    @Query("select m from Mentor m join fetch m.experiencedPositions where m.id = :mentorId")
    Optional<Mentor> findWithPositions(@Param("mentorId") Long mentorId);

    @Query("select m from Mentor m join fetch m.experiencedPositions where m.id in :mentorIds")
    List<Mentor> findAllByIds(@Param("mentorIds") List<Long> mentorIds);

}
