package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

}
