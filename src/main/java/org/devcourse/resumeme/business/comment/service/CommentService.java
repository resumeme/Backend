package org.devcourse.resumeme.business.comment.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllWithResumeId(Long resumeId) {
        return commentRepository.findAllByResumeId(resumeId);
    }

}
