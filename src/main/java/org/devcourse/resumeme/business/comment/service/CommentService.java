package org.devcourse.resumeme.business.comment.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment getOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllWithResumeId(Long resumeId) {
        return commentRepository.findAllByResumeId(resumeId);
    }

    public void update(String content, Long commentId) {
        Comment comment = getOne(commentId);
        comment.updateContent(content);
    }

    public void delete(Long commentId, Long resumeId) {
        Comment comment = getOne(commentId);
        comment.checkSameResume(resumeId);

        commentRepository.delete(comment);
    }

}
