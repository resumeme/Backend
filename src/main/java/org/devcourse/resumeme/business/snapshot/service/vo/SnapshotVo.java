package org.devcourse.resumeme.business.snapshot.service.vo;

import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.CommentSnapshotResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.ResumeSnapshotResponse;
import org.devcourse.resumeme.business.snapshot.controller.dto.SnapshotResponse;

public class SnapshotVo {

    private AllComponentResponse component;

    private CommentWithReviewResponse comment;

    private SnapshotVo(AllComponentResponse component, CommentWithReviewResponse comment) {
        this.component = component;
        this.comment = comment;
    }

    public static SnapshotVo of(AllComponentResponse component) {
        return new SnapshotVo(component, null);
    }

    public static SnapshotVo of(CommentWithReviewResponse comment) {
        return new SnapshotVo(null, comment);
    }

    public SnapshotResponse toResponse() {
        if (comment != null) {
            return new CommentSnapshotResponse(comment);
        }

        return new ResumeSnapshotResponse(component);
    }

}
