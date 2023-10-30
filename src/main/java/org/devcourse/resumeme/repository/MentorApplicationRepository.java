package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorApplicationRepository extends JpaRepository<MentorApplication, Long>  {

}
