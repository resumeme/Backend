package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.mentor.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findByEmail(String email);

}
