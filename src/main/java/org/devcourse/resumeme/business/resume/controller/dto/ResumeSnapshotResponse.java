package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;

@Data
public class ResumeSnapshotResponse implements SnapshotResponse {

    private AllComponentResponse resumeData;

    public ResumeSnapshotResponse(AllComponentResponse resumeData) {
        this.resumeData = resumeData;
    }

}
