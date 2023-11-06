package org.devcourse.resumeme.business.comment.repository;

import org.devcourse.resumeme.business.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByResumeId(Long resumeId);

}
