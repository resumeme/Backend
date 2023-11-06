package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {

}
