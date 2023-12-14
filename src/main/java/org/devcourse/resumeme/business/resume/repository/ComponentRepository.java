package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComponentRepository extends JpaRepository<Component, Long> {

    @EntityGraph(attributePaths = "components")
    List<Component> findAllByResumeId(Long resumeId);

    @Query("select c from Component c where c.resumeId = :resumeId and c.property = :type")
    Optional<Component> findExistBlock(@Param("resumeId") Long resumeId, @Param("type") Property type);

    @EntityGraph(attributePaths = "components")
    Optional<Component> findById(Long componentId);
}
