package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.review.Review;

public record ReviewResponse(Long id, String content, String blockType) {

    public ReviewResponse(Review review) {
        this(review.getId(), review.getContent(), review.getType().name());
    }

}
