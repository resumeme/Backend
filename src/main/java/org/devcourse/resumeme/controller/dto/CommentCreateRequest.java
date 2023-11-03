package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.comment.Comment;

public record CommentCreateRequest(String content, String blockType) {

    public Comment toEntity(Resume resume) {
        return new Comment(content, BlockType.valueOf(blockType.toUpperCase()), resume);
    }

}
