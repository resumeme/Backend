package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

    Optional<Snapshot> findByResumeId(Long resumeId);

}
