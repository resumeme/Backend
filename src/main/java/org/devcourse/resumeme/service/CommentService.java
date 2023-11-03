package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.comment.Comment;
import org.devcourse.resumeme.repository.CommentRepository;
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
