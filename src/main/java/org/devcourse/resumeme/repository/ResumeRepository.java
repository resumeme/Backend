package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByMenteeId(Long menteeId);
}
