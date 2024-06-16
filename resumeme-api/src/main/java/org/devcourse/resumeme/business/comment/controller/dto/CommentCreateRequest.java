package org.devcourse.resumeme.business.comment.controller.dto;

import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.resume.entity.Resume;

public record CommentCreateRequest(Long componentId, String content) {

    public Comment toEntity(Resume resume) {
        return new Comment(content, componentId, resume);
    }

}
