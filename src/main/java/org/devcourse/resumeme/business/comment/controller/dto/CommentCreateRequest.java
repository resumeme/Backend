package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.entity.Component;

public record CommentCreateRequest(Long componentId, String content, String blockType) {

    public Comment toEntity(Resume resume, Component component) {
        return new Comment(content, BlockType.valueOf(blockType.toUpperCase()), component, resume);
    }

}
