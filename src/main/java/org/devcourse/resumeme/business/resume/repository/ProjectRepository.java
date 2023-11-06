package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
