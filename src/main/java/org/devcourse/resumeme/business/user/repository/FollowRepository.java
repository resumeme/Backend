package org.devcourse.resumeme.business.user.repository;

import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByMenteeId(Long menteeId);

}
