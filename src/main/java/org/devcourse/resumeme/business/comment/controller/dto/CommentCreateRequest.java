package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Resume;

public record CommentCreateRequest(String content, String blockType) {

    public Comment toEntity(Resume resume) {
        return new Comment(content, BlockType.valueOf(blockType.toUpperCase()), resume);
    }

}
