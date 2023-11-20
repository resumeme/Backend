package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long commentId, String content, Long componentId, LocalDateTime lastModifiedAt) {

    public CommentResponse(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getComponentId(), comment.getLastModifiedDate());
    }

}
