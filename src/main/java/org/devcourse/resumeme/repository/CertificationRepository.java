package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

}
