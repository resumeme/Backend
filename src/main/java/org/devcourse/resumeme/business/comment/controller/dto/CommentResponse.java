package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;

public record CommentResponse(Long id, String content, String blockType) {

    public CommentResponse(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getType().name());
    }

}
