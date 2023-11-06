package org.devcourse.resumeme.business.user.repository.admin;

import org.devcourse.resumeme.business.user.domain.admin.MentorApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorApplicationRepository extends JpaRepository<MentorApplication, Long>  {

    @EntityGraph(attributePaths = "mentor")
    List<MentorApplication> findAll();

}
