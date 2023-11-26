package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;

@Data
public class CommentSnapshotResponse implements SnapshotResponse {

    private final CommentWithReviewResponse snapshot;

    public CommentSnapshotResponse(CommentWithReviewResponse snapshot) {
        this.snapshot = snapshot;
    }

}
