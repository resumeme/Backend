package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {

    Optional<Mentee> findByEmail(String email);

}
