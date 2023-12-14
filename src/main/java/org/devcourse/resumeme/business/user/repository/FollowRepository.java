package org.devcourse.resumeme.business.user.repository;

import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByMenteeId(Long menteeId);

    List<Follow> findAllByMentorId(Long mentorId);

    Optional<Follow> findByMenteeIdAndMentorId(Long menteeId, Long mentorId);

}
