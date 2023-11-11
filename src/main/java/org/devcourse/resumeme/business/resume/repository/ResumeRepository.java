package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByMenteeId(Long menteeId);

    long countByPassStatus(boolean passStatus);

}
