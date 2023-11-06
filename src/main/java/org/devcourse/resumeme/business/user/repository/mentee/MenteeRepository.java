package org.devcourse.resumeme.business.user.repository.mentee;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {

    Optional<Mentee> findByEmail(String email);

    @Query("select m from Mentee m join fetch m.interestedPositions join fetch m.interestedFields where m.id = :menteeId")
    Optional<Mentee> findWithPositionsAndFields(@Param("menteeId") Long menteeId);

}
