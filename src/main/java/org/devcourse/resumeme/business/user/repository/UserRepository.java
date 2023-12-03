package org.devcourse.resumeme.business.user.repository;

import org.devcourse.resumeme.business.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.userPositions left join fetch u.interestedFields where u.id = :userId")
    Optional<User> findWithPositionsAndFields(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.userPositions left join fetch u.interestedFields where u.id in :ids")
    List<User> findAllByIds(@Param("ids") List<Long> ids);

}
