package org.devcourse.resumeme.business.comment.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.devcourse.resumeme.business.snapshot.service.SnapshotCapture;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.COMMENT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final SnapshotCapture resumeCapture;

    public Comment create(Comment comment, Long resumeId) {
        captureResume(resumeId);

        return commentRepository.save(comment);
    }

    private void captureResume(Long resumeId) {
        List<Comment> comments = commentRepository.findAllByResumeId(resumeId);
        if (comments.isEmpty()) {
            resumeCapture.capture(null, resumeId);
        }
    }

    @Transactional(readOnly = true)
    public Comment getOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
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
