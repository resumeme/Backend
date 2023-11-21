package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;

import java.time.LocalDateTime;

public record CommentWithOverallReviewResponse(Long commentId, String content, Long componentId,
                                               LocalDateTime lastModifiedAt, String overallReview) {

    public CommentWithOverallReviewResponse(Comment comment, MenteeToEvent menteeToEvent) {
        this(comment.getId(), comment.getContent(), comment.getComponent().getId(), comment.getLastModifiedDate(), menteeToEvent.getOverallReview());
    }

}
