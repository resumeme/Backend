package org.devcourse.resumeme.business.comment.controller.dto;

import java.util.List;

public record CommentWithReviewResponse(List<CommentResponse> commentResponses, String overallReview, Long mentorId) {

}
