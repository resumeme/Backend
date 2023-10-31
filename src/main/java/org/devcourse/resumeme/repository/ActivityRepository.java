package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
