package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {

}
