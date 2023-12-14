package org.devcourse.resumeme.business.comment.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByResumeId(Long resumeId);

    @Modifying
    @Query("update Comment c SET c.componentId = :newComponentId where c.componentId = :oldComponentId")
    void updateComment(@Param("newComponentId") Long newComponentId, @Param("oldComponentId") Long oldComponentId);

}
