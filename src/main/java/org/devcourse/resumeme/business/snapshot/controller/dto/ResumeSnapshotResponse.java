package org.devcourse.resumeme.business.snapshot.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;

@Data
public class ResumeSnapshotResponse implements SnapshotResponse {

    private AllComponentResponse resumeData;

    public ResumeSnapshotResponse(AllComponentResponse resumeData) {
        this.resumeData = resumeData;
    }

}
