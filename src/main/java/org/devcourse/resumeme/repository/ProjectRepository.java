package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
