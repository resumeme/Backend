package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

}
