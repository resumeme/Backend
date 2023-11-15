package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByMenteeId(Long menteeId);

    long countByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @EntityGraph(attributePaths = "mentee")
    Optional<Resume> findById(Long id);

}
