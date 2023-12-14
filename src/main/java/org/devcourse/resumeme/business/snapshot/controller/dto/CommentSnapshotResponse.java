package org.devcourse.resumeme.business.snapshot.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;

@Getter
@NoArgsConstructor
public class CommentSnapshotResponse implements SnapshotResponse {

    private CommentWithReviewResponse snapshot;

    public CommentSnapshotResponse(CommentWithReviewResponse snapshot) {
        this.snapshot = snapshot;
    }

}
