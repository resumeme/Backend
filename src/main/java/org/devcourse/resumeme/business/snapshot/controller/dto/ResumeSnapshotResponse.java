package org.devcourse.resumeme.business.snapshot.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;

@Getter
@NoArgsConstructor
public class ResumeSnapshotResponse implements SnapshotResponse {

    private AllComponentResponse resumeData;

    public ResumeSnapshotResponse(AllComponentResponse resumeData) {
        this.resumeData = resumeData;
    }

}
