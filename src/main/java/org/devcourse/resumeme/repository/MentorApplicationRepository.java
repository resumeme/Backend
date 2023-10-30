package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorApplicationRepository extends JpaRepository<MentorApplication, Long>  {

    @EntityGraph(attributePaths = "mentor")
    List<MentorApplication> findWithMentorAll();

}
