package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.comment.Comment;

public record CommentResponse(Long id, String content, String blockType) {

    public CommentResponse(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getType().name());
    }

}
