package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.review.Review;

public record ReviewCreateRequest(String content, String blockType) {

    public Review toEntity(Resume resume) {
        return new Review(content, BlockType.valueOf(blockType.toUpperCase()), resume);
    }

}
